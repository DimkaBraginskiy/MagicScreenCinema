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
    public void write(JsonWriter writer, T entity) throws IOException {
        writer.beginObject();

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            Object value;

            try {
                value = field.get(entity);

                if (value == null) {
                    writer.name(field.getName()).nullValue();
                    continue;
                }

                if (PersistenceUtil.isElementCollection(field.getType())) {
                    writeSingleReference(writer, field, value);
                } else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    writeCollectionReference(writer, field, entity, value);
                } else {
                    writer.name(field.getName());
                    gson.toJson(value, field.getType(), writer);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName(), e);
            } catch (NoSuchFieldException e) {
                throw new FileNotFoundException("Field not found during cascade save: " + e.getMessage());
            }
        }

        writer.endObject();
    }

    private void writeSingleReference(JsonWriter writer, Field field, Object value) throws IOException {
        if (field.isAnnotationPresent(ManyToOne.class)) {
            Object id = PersistenceUtil.extractId(value);
            writer.name(field.getName()).value(id.toString());
        }
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

        boolean cascade = field.isAnnotationPresent(CascadeSave.class);
        List<UUID> relatedIds = new ArrayList<>();

        for (Object item : relatedEntities) {
            if (item == null) continue;

            UUID relatedId = PersistenceUtil.extractId(item);
            if (!collection.existsById(relatedId)) {
                if (cascade) saveChild(collection, item);
                else throw new ReferenceIntegrityException(
                        "Referenced entity of type " + genericType.getName() +
                                " with id " + relatedId + " does not exist.");
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

    private void saveOneToManyRelationship(Field field, Object parent, Object value)
            throws IllegalAccessException, NoSuchFieldException {

        String mappedBy = field.getAnnotation(OneToMany.class).mappedBy();
        Class<?> childType = PersistenceUtil.getGenericType(field);
        Iterable<?> children = (Iterable<?>) value;

        if (children == null) return;

        for (Object child : children) {
            if (child == null) continue;

            UUID childId = PersistenceUtil.extractId(child);
            ObjectCollection<?> childCollection = ObjectCollectionRegistry.getCollection(childType);

            if (!childCollection.existsById(childId) && !field.isAnnotationPresent(CascadeSave.class)) {
                throw new ReferenceIntegrityException(
                        "Referenced entity of type " + childType.getName() +
                                " with id " + childId + " does not exist.");
            }

            Field mappedField = child.getClass().getDeclaredField(mappedBy);
            mappedField.setAccessible(true);

            if (!mappedField.getType().equals(type)) {
                throw new RelationshipDeclarationException(
                        "MappedBy field type mismatch for " + field.getName() +
                                ": expected " + type.getName() + ", found " + mappedField.getType().getName());
            }

            mappedField.set(child, parent);
            saveChild(childCollection, child);
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
    }

    private void readSingleReference(Field field, Object instance, JsonReader reader)
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

        if (!field.isAnnotationPresent(Eager.class)) {
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
        if (!field.isAnnotationPresent(Eager.class)) {
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
}
