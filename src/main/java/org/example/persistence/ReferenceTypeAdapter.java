package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.example.persistence.exception.DeserializationException;
import org.example.persistence.exception.ReferenceIntegrityException;
import org.example.persistence.exception.RelationshipDeclarationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

class ReferenceTypeAdapter<T> extends TypeAdapter<T> {
    private final Gson gson;
    private final Class<T> type;

    public ReferenceTypeAdapter(Gson gson, Class<T> type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public void write(JsonWriter jsonWriter, T entityToWrite) throws IOException {
        jsonWriter.beginObject();
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(entityToWrite);

                if (PersistenceUtil.isElementCollection(field.getType())) {
                    Object id = PersistenceUtil.extractId(value);
                    jsonWriter.name(field.getName()).value(id.toString());
                }
                else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    if(field.isAnnotationPresent(OneToMany.class)) {
                        saveOneToManyRelationship(field, entityToWrite, value);
                        writeEmptyArray(jsonWriter, field.getName());
                    }
                    if(field.isAnnotationPresent(ManyToMany.class)){
                        saveManyToManyRelationship(field, entityToWrite, value);
                        writeEmptyArray(jsonWriter, field.getName());
                    }
                }
                else{
                    jsonWriter.name(field.getName());
                    gson.toJson(value, field.getType(), jsonWriter);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName(), e);
            } catch (NoSuchFieldException e) {
                throw new FileNotFoundException("Could not find field during cascade save: " + e.getMessage());
            }
        }
        jsonWriter.endObject();
    }

    private void writeEmptyArray(JsonWriter jsonWriter, String name) throws IOException {
        jsonWriter.name(name);
        jsonWriter.beginArray();
        jsonWriter.endArray();
    }

    private void saveManyToManyRelationship(Field field, Object entityToWrite, Object value) throws IllegalAccessException, NoSuchFieldException, IOException {
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(PersistenceUtil.getGenericType(field));
        Collection<?> childCollection = (Collection<?>) value;
        Class<?> genericType = PersistenceUtil.getGenericType(field);
        boolean isCascade = field.isAnnotationPresent(CascadeSave.class);

        JoinCollectionManager joinCollectionManager;
        List<UUID> relatedIds = new ArrayList<>();

        if(childCollection == null) return;
        for(Object item : childCollection){
            UUID relatedId = PersistenceUtil.extractId(item);
            if(!collection.existsById(relatedId)){
                if(isCascade) saveChild(collection, item);
                else throw new ReferenceIntegrityException("Referenced entity of type " + genericType.getName() + " with id " + relatedId + " does not exist.");
            }
            relatedIds.add(relatedId);
        }
        if(field.isAnnotationPresent(JoinCollection.class)){
            joinCollectionManager = getCollectionManger(field);
            joinCollectionManager.saveRelations(PersistenceUtil.extractId(entityToWrite), relatedIds);
        } else {
            Field mappedField = validateManyToManyMappedField(field, genericType);
            joinCollectionManager = getCollectionManger(mappedField);
            joinCollectionManager.saveRelationsInverse(relatedIds, PersistenceUtil.extractId(entityToWrite));
        }
    }

    private Field validateManyToManyMappedField(Field field, Class<?> genericType) throws NoSuchFieldException {
        String mappedBy = field.getAnnotation(ManyToMany.class).mappedBy();
        if(mappedBy.isEmpty()) throw new RelationshipDeclarationException("ManyToMany relationship must have JoinCollection annotation or mappedBy defined on field " + field.getName());

        Field mappedField = genericType.getDeclaredField(mappedBy);
        if(!Objects.equals(PersistenceUtil.getGenericType(mappedField), type) || mappedField.getAnnotation(ManyToMany.class) == null){
            throw new RelationshipDeclarationException("ManyToMany relationship is not properly declared on field " + field.getName());
        }
        if(!mappedField.isAnnotationPresent(JoinCollection.class)){
            throw new RelationshipDeclarationException("Field " + mappedField.getName() + " must have JoinCollection annotation.");
        }
        return mappedField;
    }

    private static JoinCollectionManager getCollectionManger(Field field) {
        JoinCollection joinCollection = field.getAnnotation(JoinCollection.class);
        if(joinCollection == null || joinCollection.name().isEmpty()){
            throw new RelationshipDeclarationException("JoinCollection annotation must have name, joinAttribute and inverseJoinAttribute defined.");
        }

        return JoinCollectionManagerRegistry.getManager(joinCollection.name());
    }

    private void saveOneToManyRelationship(Field field, Object entityToWrite, Object value) throws IllegalAccessException, NoSuchFieldException {
        String mappedBy = field.getAnnotation(OneToMany.class).mappedBy();
        Class<?> genType = PersistenceUtil.getGenericType(field);
        Iterable<?> iterable = (Iterable<?>) value;

        if (iterable == null) return;

        for (Object item : iterable) {
            UUID childId = PersistenceUtil.extractId(item);

            ObjectCollection<?> childCollection = ObjectCollectionRegistry.getCollection(genType);
            if (!childCollection.existsById(childId) && !field.isAnnotationPresent(CascadeSave.class)) {
                throw new ReferenceIntegrityException("Referenced entity of type " + genType.getName() + " with id " + childId + " does not exist.");

            }
            Field mappedField = item.getClass().getDeclaredField(mappedBy);
            mappedField.setAccessible(true);
            mappedField.set(item, entityToWrite);
            saveChild(childCollection, item);
        }
    }

    private <C> void saveChild(ObjectCollection<C> collection, Object child) {
        collection.save((C) child);
    }


    @Override
    public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        T instance;
        try {
            instance = type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new DeserializationException("Could not create instance of " + type.getName(), e);
        }

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
                    ;
                }
                else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    if(field.isAnnotationPresent(OneToMany.class)) {
                        readOneToManyRelationship(field, instance, id);
                        reader.skipValue();
                    }
                    else if (field.isAnnotationPresent(ManyToMany.class)) {
                        reader.skipValue();
                        readManyToManyRelationship(field, instance, id);
                    }
                }
                else {
                    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(field.getGenericType()));
                    field.set(instance, adapter.read(reader));
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                reader.skipValue();
            }
        }
        reader.endObject();
        return instance;
    }

    private void readSingleReference(Field field, Object instance, JsonReader reader) throws IllegalAccessException, IOException {
        UUID refId = gson.fromJson(reader, PersistenceUtil.findIdField(field.getType()).getType());
        Object existing = PersistenceContext.getFromContext(field.getType(), refId);

        if (existing != null) {
            field.set(instance, existing);
        } else {
            ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
            Object ref = collection.findById(refId, false).orElse(null);
            if (ref != null) PersistenceContext.registerInContext(ref);
            field.set(instance, ref);
        }
    }

    private void readOneToManyRelationship(Field field, Object instance, UUID id) throws NoSuchFieldException, IllegalAccessException {
        if (!field.isAnnotationPresent(Eager.class)) return;

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        ObjectCollection<?> collectionGeneric = ObjectCollectionRegistry.getCollection(genericType);
        List<Object> children = new ArrayList<>();

        for (Object child : collectionGeneric.findAll()) {
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

    private void readManyToManyRelationship(Field field, Object instance, UUID id) throws IllegalAccessException, IOException, NoSuchFieldException {
        System.out.println("Reading ManyToMany relationship for field: " + field.getName());
        List<Object> relatedEntities = new ArrayList<>();

        if(field.isAnnotationPresent(Eager.class)) {

            JoinCollectionManager joinCollectionManager;
            List<UUID> relatedIds;
            Class<?> genericType = PersistenceUtil.getGenericType(field);

            if (field.isAnnotationPresent(JoinCollection.class)) {
                joinCollectionManager = getCollectionManger(field);
                relatedIds = joinCollectionManager.getRelatedIds(id);
            }
            else {
                Field mappedField = validateManyToManyMappedField(field, PersistenceUtil.getGenericType(field));
                joinCollectionManager = getCollectionManger(mappedField);
                relatedIds = joinCollectionManager.getRelatedIdsInverse(id);
            }

            ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(PersistenceUtil.getGenericType(field));

            for (UUID relatedId : relatedIds) {
                Object relatedEntity = PersistenceContext.getFromContext(genericType, relatedId);
                if (relatedEntity != null) {
                    relatedEntities.add(relatedEntity);
                    continue;
                }
                relatedEntity = collection.findById(relatedId, false).orElse(null);
                if (relatedEntity != null) {
                    relatedEntities.add(relatedEntity);
                    PersistenceContext.registerInContext(relatedEntity);
                }
            }
        }
        field.set(instance, relatedEntities);
    }
}
