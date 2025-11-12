package com.magicscreencinema.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;
import com.magicscreencinema.persistence.declaration.*;
import com.magicscreencinema.persistence.exception.*;

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
                    writeSingleReference(currentField, entityToSave, currentFieldValue);
                    writer.name(currentField.getName()).nullValue();
                } else if (PersistenceUtil.isCollectionOfElementCollection(currentField)) {
                    writeCollectionReference(currentField, entityToSave, currentFieldValue);
                    writeEmptyArray(writer, currentField.getName());
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

    private void writeSingleReference(Field currentField, Object entityToSave, Object currentValue) throws IOException, IllegalAccessException {
        if (currentField.isAnnotationPresent(ManyToOne.class)) {
            saveManyToOneRelationship(currentField, entityToSave, currentValue);
        } else if (currentField.isAnnotationPresent(OneToOne.class)) {
            saveOneToOneRelationship(currentField, entityToSave, currentValue);
        }
    }

    private void writeCollectionReference(Field field, Object entity, Object value)
            throws IllegalAccessException, NoSuchFieldException, IOException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            saveOneToManyRelationship(field, entity, value);
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            saveManyToManyRelationship(field, entity, value);
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

        boolean cascade = isCascadeSave(field.getAnnotation(ManyToMany.class).cascade());
        List<UUID> relatedIds = new ArrayList<>();

        PersistenceContext.registerInContext(entity);

        for (Object item : relatedEntities) {
            if (item == null) continue;

            UUID relatedId = PersistenceUtil.extractId(item);
            Object existingInContext = PersistenceContext.getFromContext(genericType, relatedId);

            if (!collection.existsById(relatedId) && existingInContext == null && !cascade) {
                throw new ReferenceIntegrityException("Referenced entity of type " + genericType.getName() + " with id " + relatedId + " does not exist.");
            }
            if (cascade && existingInContext == null) saveChild(collection, item);
            relatedIds.add(relatedId);
        }

        ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, genericType);
        ;
        if (field.isAnnotationPresent(Owner.class)) {
            manager.saveRelations(PersistenceUtil.extractId(entity), relatedIds);
        } else {
            manager.saveRelationsInverse(relatedIds, PersistenceUtil.extractId(entity));
        }
    }

    private void saveOneToManyRelationship(Field currentField, Object parent, Object currentFieldValue) throws IOException {
        PersistenceContext.registerInContext(parent);

        Cascade[] cascade = currentField.getAnnotation(OneToMany.class).cascade();

        Class<?> childType = PersistenceUtil.getGenericType(currentField);
        ObjectCollection<?> childCollection = ObjectCollectionRegistry.getCollection(childType);
        Iterable<?> children = (Iterable<?>) currentFieldValue;

        if (children == null) return;

        for (Object child : children) {
            if (child == null) continue;

            ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, childType);

            UUID childId = PersistenceUtil.extractId(child);

            if (!childCollection.existsById(childId)) {
                if (!isCascadeSave(cascade))
                    throw new ReferenceIntegrityException("Referenced entity of type " + childType.getName() + " with id " + childId + " does not exist.");
                else saveChild(childCollection, child);
            }

            List<UUID> childRelatedIds = manager.getRelatedIds(childId, true);
            if (childRelatedIds.isEmpty()) {
                manager.saveRelation(childId, PersistenceUtil.extractId(parent));
            } else {
                manager.replaceRelation(childId, childRelatedIds.getFirst(), PersistenceUtil.extractId(parent));
            }
        }
    }

    private void saveManyToOneRelationship(Field currentField, Object entityToSave, Object currentValue) throws IOException {
        UUID id = PersistenceUtil.extractId(currentValue);
        Class<?> currentFieldType = currentField.getType();
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(currentFieldType);

        Object existing = PersistenceContext.getFromContext(currentFieldType, id);

        if (!collection.existsById(id) && existing == null)
            throw new ReferenceIntegrityException("Referenced entity of type " +
                    currentField.getType().getName() + " with id " + id + " does not exist.");

        ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, currentFieldType);
        List<UUID> relatedIds = manager.getRelatedIds(PersistenceUtil.extractId(entityToSave), true);

        if (relatedIds.isEmpty())
            manager.saveRelation(PersistenceUtil.extractId(entityToSave), PersistenceUtil.extractId(currentValue));
        else
            manager.replaceRelation(PersistenceUtil.extractId(entityToSave), relatedIds.getFirst(), id);
    }

    private void saveOneToOneRelationship(Field currentField, Object entityToSave, Object currentValue)
            throws IOException {
        UUID idOfCurrentField = PersistenceUtil.extractId(currentValue);
        UUID entityToSaveId = PersistenceUtil.extractId(entityToSave);
        UUID ownerId;
        UUID dependentId;

        Class<?> currentFieldType = currentField.getType();
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(currentFieldType);
        Cascade[] cascade = currentField.getAnnotation(OneToOne.class).cascade();
        boolean isSave = isCascadeSave(cascade);

        PersistenceContext.registerInContext(entityToSave);
        Object existingInContext = PersistenceContext.getFromContext(currentFieldType, idOfCurrentField);

        if (!collection.existsById(idOfCurrentField) && existingInContext == null && !isSave) {
            throw new ReferenceIntegrityException("Referenced entity of type " + currentFieldType.getName() +
                    " with id " + idOfCurrentField + " does not exist.");
        }

        List<UUID> relatedIds;
        ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, currentFieldType);

        if (currentField.isAnnotationPresent(Owner.class)) {
            ownerId = entityToSaveId;
            dependentId = idOfCurrentField;
            relatedIds = manager.getRelatedIds(entityToSaveId, true);
        } else {
            ownerId = idOfCurrentField;
            dependentId = entityToSaveId;
            relatedIds = manager.getRelatedIds(entityToSaveId, false);
        }
        if (isSave) saveChild(collection, currentValue);

        if (relatedIds.isEmpty())
            manager.saveRelation(ownerId, dependentId);
        else
            manager.replaceRelation(ownerId, relatedIds.getFirst(), dependentId);
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

        T instance = createInstance();
        UUID instanceId = null;
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
                    instanceId = gson.fromJson(reader, field.getType());
                    field.set(instance, instanceId);
                    PersistenceContext.registerSubContext(type, instanceId, instance);
                    continue;
                }

                if (PersistenceUtil.isElementCollection(field.getType())) {
                    readSingleReference(field, instance, instanceId);
                    reader.skipValue();
                } else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    readCollectionReference(field, instance, instanceId);
                    reader.skipValue();
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
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new MissingNoArgsConstructorException(
                    "Class " + type.getName() + " must have a public or accessible no-argument constructor");
        } catch (Exception e) {
            throw new DeserializationException(
                    "Could not create instance of " + type.getName(), e
            );
        }
    }

    private void readCollectionReference(Field field, Object instance, UUID id)
            throws IOException, IllegalAccessException, NoSuchFieldException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            readOneToManyRelationship(field, instance, id);
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            readManyToManyRelationship(field, instance, id);
        } else {
            throw new RelationshipDeclarationException(
                    "Collection field " + field.getName() +
                            " must have OneToMany or ManyToMany annotation.");
        }
    }

    private void readSingleReference(Field field, Object instance, UUID instanceId)
            throws IllegalAccessException, IOException {
        if (field.isAnnotationPresent(ManyToOne.class)) {
            readManyToOneRelationship(field, instance, instanceId);
        } else if (field.isAnnotationPresent(OneToOne.class)) {
            readOneToOneRelationship(field, instance, instanceId, field.isAnnotationPresent(Owner.class));
        } else {
            throw new RelationshipDeclarationException(
                    "ElementCollection field " + field.getName() +
                            " must have ManyToOne or OneToOne annotation.");
        }
    }

    private void readManyToOneRelationship(Field field, Object instance, UUID instanceId) throws IllegalAccessException, IOException {
        readOneToOneRelationship(field, instance, instanceId, true);
    }

    private void readOneToManyRelationship(Field field, Object instance, UUID instanceId)
            throws IllegalAccessException, IOException {
        if (!field.getAnnotation(OneToMany.class).fetch().equals(Fetch.EAGER)) {
            field.set(instance, Collections.emptyList());
            return;
        }

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        if (genericType == null) return;

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);
        List<Object> children = new ArrayList<>();

        List<UUID> childrenIds = ReferenceCollectionManagerRegistry.getManager(type, genericType)
                .getRelatedIds(instanceId, false);

        for (UUID childId : childrenIds) {
            collection.findById(childId).ifPresent(children::add);
        }

        field.set(instance, children);
    }

    private void readManyToManyRelationship(Field field, Object instance, UUID id) throws IllegalAccessException, IOException {
        List<Object> relatedEntities = new ArrayList<>();
        if (!field.getAnnotation(ManyToMany.class).fetch().equals(Fetch.EAGER)) {
            field.set(instance, relatedEntities);
            return;
        }

        Class<?> genericType = PersistenceUtil.getGenericType(field);
        ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, genericType);
        List<UUID> relatedIds;

        if (field.isAnnotationPresent(Owner.class)) {
            relatedIds = manager.getRelatedIds(id, true);
        } else {
            relatedIds = manager.getRelatedIds(id, false);
        }

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);

        for (UUID relatedId : relatedIds) {
            Object relatedEntity = PersistenceContext.getFromContext(genericType, relatedId);
            if (relatedEntity == null) {
                relatedEntity = collection.findById(relatedId).orElse(null);
                if (relatedEntity != null) PersistenceContext.registerInContext(relatedEntity);
            }
            if (relatedEntity != null) relatedEntities.add(relatedEntity);
        }

        field.set(instance, relatedEntities);
    }

    private void readOneToOneRelationship(Field field, Object instance, UUID instanceId, boolean isOwner)
            throws IllegalAccessException, IOException {
        ReferenceCollectionManager manager = ReferenceCollectionManagerRegistry.getManager(type, field.getType());
        List<UUID> relatedIds = manager.getRelatedIds(instanceId, isOwner);
        if (relatedIds.isEmpty()) {
            field.set(instance, null);
            return;
        }

        UUID refId = relatedIds.getFirst();
        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
        Object existing = PersistenceContext.getFromContext(field.getType(), refId);

        if (existing != null) {
            field.set(instance, existing);
            return;
        }

        Object found = collection.findById(refId).orElse(null);
        if (found != null) PersistenceContext.registerInContext(found);

        field.set(instance, found);
    }

    private boolean isCascadeSave(Cascade[] cascade) {
        return Arrays.stream(cascade).anyMatch(c -> c == Cascade.SAVE);
    }
}
