package org.example.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;
import org.example.persistence.exception.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Custom Gson TypeAdapter to handle complex persistence relationships
 * (OneToMany, ManyToMany, etc.) and reference resolution.
 */
class ReferenceTypeAdapter<T> extends TypeAdapter<T> {

    private final Gson gson;
    private final Class<T> type;

    public ReferenceTypeAdapter(Gson gson, Class<T> type) {
        this.gson = gson;
        this.type = type;
    }


    @Override
    public void write(JsonWriter writer, T entityToSave) throws IOException {
        writer.beginObject();

        for (Field currentField : type.getDeclaredFields()) {
            currentField.setAccessible(true);
            Object currentFieldValue;

            try {
                currentFieldValue = currentField.get(entityToSave);

                if (currentFieldValue == null) {
                    writer.name(currentField.getName()).nullValue();
                    continue;
                }

                if (PersistenceUtil.isElementCollection(currentField.getType())) {
                    writeSingleReference(writer, currentField, entityToSave, currentFieldValue);
                } else if (PersistenceUtil.isCollectionOfElementCollection(currentField)) {
                    writeCollectionReference(writer, currentField, entityToSave, currentFieldValue);
                } else {
                    writer.name(currentField.getName());
                    gson.toJson(currentFieldValue, currentField.getType(), writer);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + currentField.getName(), e);
            } catch (NoSuchFieldException e) {
                throw new FileNotFoundException("Field not found during cascade save: " + e.getMessage());
            }
        }

        writer.endObject();
    }

    private void writeSingleReference(JsonWriter writer, Field currentField, Object entityToSave, Object currentValue) throws IOException, IllegalAccessException {
        if (currentField.isAnnotationPresent(ManyToOne.class)) {
            saveManyToOneRelationship(writer, currentField, currentValue);
        }
        else if(currentField.isAnnotationPresent(OneToOne.class)){
            saveOneToOneRelationship(writer, currentField, entityToSave, currentValue);
        }
    }

    private Field getMappedFieldForOneToOneRelationship(Field field, Class<?> relatedType) throws FileNotFoundException {
        String mappedBy = field.getAnnotation(OneToOne.class).mappedBy();
        if(mappedBy == null || mappedBy.isEmpty()){
            throw new RelationshipDeclarationException("OneToOne relationship must have ForeignKey or mappedBy defined.");
        }
        Field mappedField = null;
        try {
            mappedField = relatedType.getDeclaredField(mappedBy);
        } catch (NoSuchFieldException e) {
            throw new FileNotFoundException("Field " + mappedBy + " not found in class " + relatedType.getName() + ". Check mappedBy declaration.");
        }
        if(!mappedField.getType().equals(type)){
            throw new RelationshipDeclarationException("MappedBy field type mismatch for " + field.getName() +
                    ": expected " + type.getName() + ", found " + mappedField.getType().getName());
        }
        return mappedField;
    }

    private void writeCollectionReference(JsonWriter writer, Field field, Object entity, Object value)
            throws IllegalAccessException, NoSuchFieldException, IOException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            saveOneToManyRelationship(field, entity, value);
            writeEmptyArray(writer, field.getName());
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            saveManyToManyRelationship(field, entity, value);
            writeEmptyArray(writer, field.getName());
        }
    }

    private void writeEmptyArray(JsonWriter writer, String name) throws IOException {
        writer.name(name);
        writer.beginArray();
        writer.endArray();
    }

    private void saveManyToManyRelationship(Field field, Object entity, Object value)
            throws IOException {

        Collection<?> relatedEntities = (Collection<?>) value;
        if (relatedEntities == null) return;

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);

        boolean cascade = field.getAnnotation(ManyToMany.class).cascade().equals(Cascade.SAVE);
        List<UUID> relatedIds = new ArrayList<>();

        PersistenceContext.registerInContext(entity);

        for (Object item : relatedEntities) {
            if (item == null) continue;

            UUID relatedId = PersistenceUtil.extractId(item);
            Object existingInContext = PersistenceContext.getFromContext(genericType, relatedId);

            if (!collection.existsById(relatedId) && existingInContext == null && !cascade) {
                throw new ReferenceIntegrityException(
                        "Referenced entity of type " + genericType.getName() +
                                " with id " + relatedId + " does not exist.");
            }
            if (cascade && existingInContext == null) {
                saveChild(collection, item);
            }

            relatedIds.add(relatedId);
        }

        JoinCollectionManager manager;
        if (field.isAnnotationPresent(JoinCollection.class)) {
            manager = getCollectionManager(field);
            manager.saveRelations(PersistenceUtil.extractId(entity), relatedIds);
        } else {
            String mappedBy = field.getAnnotation(ManyToMany.class).mappedBy();
            Field mappedField = validateManyToManyMappedField(mappedBy, genericType);
            manager = getCollectionManager(mappedField);
            manager.saveRelationsInverse(relatedIds, PersistenceUtil.extractId(entity));
        }
    }

    private void saveOneToManyRelationship(Field currentField, Object parent, Object currentFieldValue)
            throws IllegalAccessException, NoSuchFieldException {
        PersistenceContext.registerInContext(parent);

        String mappedBy = currentField.getAnnotation(OneToMany.class).mappedBy();
        Cascade cascade = currentField.getAnnotation(OneToMany.class).cascade();

        Class<?> childType = PersistenceUtil.getGenericType(currentField);
        Iterable<?> children = (Iterable<?>) currentFieldValue;

        if (children == null) return;

        ObjectCollection<?> childCollection = ObjectCollectionRegistry.getCollection(childType);

        for (Object child : children) {
            if (child == null) continue;

            UUID childId = PersistenceUtil.extractId(child);

            if (!childCollection.existsById(childId) && !cascade.equals(Cascade.SAVE)) {
                throw new ReferenceIntegrityException(
                        "Referenced entity of type " + childType.getName() +
                                " with id " + childId + " does not exist.");
            }

            Field mappedField = child.getClass().getDeclaredField(mappedBy);
            mappedField.setAccessible(true);

            if (!mappedField.getType().equals(type)) {
                throw new RelationshipDeclarationException(
                        "MappedBy field type mismatch for " + currentField.getName() +
                                ": expected " + type.getName() + ", found " + mappedField.getType().getName());
            }

            mappedField.set(child, parent);
            saveChild(childCollection, child);
        }
    }
    private void saveManyToOneRelationship(JsonWriter writer, Field currentField, Object currentValue) throws IOException {
        UUID id = PersistenceUtil.extractId(currentValue);
        Class<?> currentFieldType = currentField.getType();
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(currentFieldType);

        Object existing = PersistenceContext.getFromContext(currentFieldType, id);

        if(!collection.existsById(id) && existing == null)
            throw new ReferenceIntegrityException("Referenced entity of type " +
                    currentField.getType().getName() + " with id " + id + " does not exist.");

        writer.name(currentField.getName()).value(id.toString());
    }

    private void saveOneToOneRelationship(JsonWriter writer, Field currentField, Object entityToSave, Object currentValue)
            throws IllegalAccessException, IOException {
        UUID idOfCurrentField = PersistenceUtil.extractId(currentValue);
        Class<?> currentFieldType = currentField.getType();
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(currentFieldType);
        Cascade cascade = currentField.getAnnotation(OneToOne.class).cascade();

        PersistenceContext.registerInContext(entityToSave);
        Object existingInContext = PersistenceContext.getFromContext(currentFieldType, idOfCurrentField);

        if(!collection.existsById(idOfCurrentField) && existingInContext == null && !cascade.equals(Cascade.SAVE)){

            throw new ReferenceIntegrityException("Referenced entity of type " + currentFieldType.getName() +
                    " with id " + idOfCurrentField + " does not exist.");
        }

        if(currentField.isAnnotationPresent(ForeignKey.class)){
            writer.name(currentField.getName()).value(idOfCurrentField.toString());
            if(cascade.equals(Cascade.SAVE) && existingInContext == null) saveChild(collection, currentValue);
        }
        else{
            Field mappedField = getMappedFieldForOneToOneRelationship(currentField, currentFieldType);
            mappedField.setAccessible(true);
            mappedField.set(currentValue, entityToSave);
            writer.name(currentField.getName()).nullValue();
            saveChild(collection, currentValue);
        }
    }

    private <C> void saveChild(ObjectCollection<C> collection, Object child) {
        collection.save((C) child);
    }

    private static JoinCollectionManager getCollectionManager(Field field) {
        JoinCollection joinCollection = field.getAnnotation(JoinCollection.class);
        if (joinCollection == null || joinCollection.name().isEmpty()) {
            throw new RelationshipDeclarationException(
                    "JoinCollection annotation must define name, joinAttribute, and inverseJoinAttribute.");
        }
        return JoinCollectionManagerRegistry.getManager(joinCollection.name());
    }

    private Field validateManyToManyMappedField(String mappedBy, Class<?> genericType) {
        if (mappedBy.isEmpty()) {
            throw new RelationshipDeclarationException(
                    "ManyToMany relationship must have JoinCollection or mappedBy defined.");
        }

        Field mappedField;
        try {
            mappedField = genericType.getDeclaredField(mappedBy);
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundException(
                    "Field " + mappedBy + " not found in class " + type.getName(), e);
        }

        if (!Objects.equals(PersistenceUtil.getGenericType(mappedField), type) ||
                mappedField.getAnnotation(ManyToMany.class) == null) {
            throw new RelationshipDeclarationException(
                    "Improperly declared ManyToMany relationship on field " + mappedField.getName());
        }

        if (!mappedField.isAnnotationPresent(JoinCollection.class)) {
            throw new RelationshipDeclarationException(
                    "Field " + mappedField.getName() + " must have JoinCollection annotation.");
        }

        return mappedField;
    }

    @Override
    public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        T instance = createInstance();
        UUID id = null;
        Field idField = PersistenceUtil.findIdField(type);

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            try {
                Field field = type.getDeclaredField(name);
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    reader.skipValue();
                    continue;
                }

                field.setAccessible(true);

                if (field.equals(idField)) {
                    id = gson.fromJson(reader, field.getType());
                    field.set(instance, id);
                    PersistenceContext.registerSubContext(type, id, instance);
                    continue;
                }

                if (PersistenceUtil.isElementCollection(field.getType())) {
                    readSingleReference(field, instance, reader);
                } else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    handleCollectionReference(field, instance, id, reader);
                } else {
                    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(field.getGenericType()));
                    field.set(instance, adapter.read(reader));
                }

            } catch (IllegalAccessException e) {
                throw new DeserializationException(
                        "Could not access field " + name + " of class " + type.getName(), e);
            } catch (NoSuchFieldException e) {
                throw new FieldNotFoundException(
                        "Field " + name + " not found in class " + type.getName(), e);
            }
        }

        reader.endObject();
        return instance;
    }

    private T createInstance() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new DeserializationException("Could not create instance of " + type.getName(), e);
        }
    }

    private void handleCollectionReference(Field field, Object instance, UUID id, JsonReader reader)
            throws IOException, IllegalAccessException, NoSuchFieldException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            readOneToManyRelationship(field, instance, id);
            reader.skipValue();
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            reader.skipValue();
            readManyToManyRelationship(field, instance, id);
        }
        else {
            throw new RelationshipDeclarationException(
                    "Collection field " + field.getName() +
                            " must have OneToMany or ManyToMany annotation.");
        }
    }

    private void readSingleReference(Field field, Object instance, JsonReader reader)
            throws IllegalAccessException {
        if(field.isAnnotationPresent(ManyToOne.class)){
            readManyToOneRelationship(field, instance, reader);
        }
        else if(field.isAnnotationPresent(OneToOne.class)){
            readOneToOneRelationship(field, instance, reader);
        }
        else {
            throw new RelationshipDeclarationException(
                    "ElementCollection field " + field.getName() +
                            " must have ManyToOne or OneToOne annotation.");
        }
    }

    private void readManyToOneRelationship(Field field, Object instance, JsonReader reader)
            throws IllegalAccessException {

        UUID refId = gson.fromJson(reader, PersistenceUtil.findIdField(field.getType()).getType());
        if (refId == null) {
            field.set(instance, null);
            return;
        }

        Object existing = PersistenceContext.getFromContext(field.getType(), refId);
        if (existing != null) {
            field.set(instance, existing);
            return;
        }

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
        Object ref = collection.findById(refId, false).orElse(null);

        if (ref != null) PersistenceContext.registerInContext(ref);
        field.set(instance, ref);
    }

    private void readOneToManyRelationship(Field field, Object instance, UUID id)
            throws NoSuchFieldException, IllegalAccessException {

        if (!field.getAnnotation(OneToMany.class).fetch().equals(Fetch.EAGER)) {
            field.set(instance, Collections.emptyList());
            return;
        }

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        if (genericType == null) return;

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);
        List<Object> children = new ArrayList<>();

        for (Object child : collection.findAll(false)) {
            Field mappedField = genericType.getDeclaredField(field.getAnnotation(OneToMany.class).mappedBy());
            mappedField.setAccessible(true);

            Object parent = mappedField.get(child);
            if (parent != null && PersistenceUtil.extractId(parent).equals(id)) {
                children.add(child);
                PersistenceContext.registerInContext(child);
            }
        }

        field.set(instance, children);
    }

    private void readManyToManyRelationship(Field field, Object instance, UUID id)
            throws IllegalAccessException, IOException {

        List<Object> relatedEntities = new ArrayList<>();
        if (!field.getAnnotation(ManyToMany.class).fetch().equals(Fetch.EAGER)) {
            field.set(instance, relatedEntities);
            return;
        }

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        JoinCollectionManager manager;
        List<UUID> relatedIds;

        if (field.isAnnotationPresent(JoinCollection.class)) {
            manager = getCollectionManager(field);
            relatedIds = manager.getRelatedIds(id);
        } else {
            String mappedBy = field.getAnnotation(ManyToMany.class).mappedBy();
            Field mappedField = validateManyToManyMappedField(mappedBy, genericType);
            manager = getCollectionManager(mappedField);
            relatedIds = manager.getRelatedIdsInverse(id);
        }

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);

        for (UUID relatedId : relatedIds) {
            Object relatedEntity = PersistenceContext.getFromContext(genericType, relatedId);
            if (relatedEntity == null) {
                relatedEntity = collection.findById(relatedId, false).orElse(null);
                if (relatedEntity != null) PersistenceContext.registerInContext(relatedEntity);
            }
            if (relatedEntity != null) relatedEntities.add(relatedEntity);
        }

        field.set(instance, relatedEntities);
    }

    private void readOneToOneRelationship(Field field, Object instance, JsonReader reader)
            throws IllegalAccessException{
        if(field.isAnnotationPresent(ForeignKey.class)){
            readManyToOneRelationship(field, instance, reader);
        }
        else{
            ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
            UUID instanceId = PersistenceUtil.extractId(instance);
            List<?> results = collection.findAll(false);
            Object found = null;
            for (Object entity : results) {
                Field mappedField;
                try {
                    mappedField = field.getType().getDeclaredField(field.getAnnotation(OneToOne.class).mappedBy());
                } catch (NoSuchFieldException e) {
                    throw new FieldNotFoundException(
                            "Field " + field.getAnnotation(OneToOne.class).mappedBy() +
                                    " not found in class " + field.getType().getName(), e);
                }
                mappedField.setAccessible(true);

                Object parent = mappedField.get(entity);
                if (parent != null && PersistenceUtil.extractId(parent).equals(instanceId)) {
                    found = entity;
                    PersistenceContext.registerInContext(found);
                    break;
                }
            }
            field.set(instance, found);
        }
    }
}
