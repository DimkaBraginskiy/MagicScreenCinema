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

        List<Class<?>> hierarchy = new ArrayList<>();
        Class<?> currentType = entityToSave.getClass();
        while (currentType != null && currentType != Object.class) {
            hierarchy.add(0, currentType);
            currentType = currentType.getSuperclass();
        }

        for (Class<?> clazz : hierarchy) {
            for (Field currentField : clazz.getDeclaredFields()) {
                currentField.setAccessible(true);

                if (Modifier.isStatic(currentField.getModifiers()) || currentField.isSynthetic()) {
                    continue;
                }

                try {
                    Object currentFieldValue = currentField.get(entityToSave);
                    writer.name(currentField.getName());

                    if (currentFieldValue == null) {
                        writer.nullValue();
                        continue;
                    }

                    if (PersistenceUtil.isElementCollection(currentField.getType())) {
                        writeSingleReference(currentField, entityToSave, currentFieldValue);
                        writer.nullValue();
                    } else if (PersistenceUtil.isCollectionOfElementCollection(currentField)) {
                        Class<?> childType = PersistenceUtil.getGenericType(currentField);
                        writeCollectionReference(childType, currentField, entityToSave, (Collection<?>) currentFieldValue);
                        writeEmptyArray(writer);
                    } else if (PersistenceUtil.isQualified(currentField)) {
                        writeQualifiedReference(currentField, entityToSave, currentFieldValue);
                        writeEmptyArray(writer);
                    } else {
                        gson.toJson(currentFieldValue, currentField.getType(), writer);
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not access field " + currentField.getName(), e);
                } catch (NoSuchFieldException e) {
                    throw new FileNotFoundException("Field not found during cascade save: " + e.getMessage());
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
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

    private void writeCollectionReference(Class<?> childType, Field field, Object entity, Collection<?> value)
            throws IllegalAccessException, NoSuchFieldException, IOException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            saveOneToManyRelationship(childType, field, entity, value);
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            saveManyToManyRelationship(childType, field, entity, value);
        }
    }

    private <K> void writeQualifiedReference(Field field, Object entity, Object value)
            throws IOException, NoSuchFieldException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {

        Qualifier qualifierAnn = field.getAnnotation(Qualifier.class);
        Class<? extends QualifierKeyConverter<K>> converterClass = (Class<? extends QualifierKeyConverter<K>>) qualifierAnn.converter();
        String collectionName = qualifierAnn.referenceCollectionName();

        QualifierKeyConverter<K> converter = converterClass.getDeclaredConstructor().newInstance();

        QualifiedReferenceCollectionManager<K> manager =
                QualifiedReferenceCollectionManagerRegistry.getManager(converter, collectionName);

        Map<K, ?> values = (Map<K, ?>) value;

        for (Map.Entry<K, ?> entry : values.entrySet()) {
            K qualifierKey = entry.getKey();
            UUID relatedId = PersistenceUtil.extractId(entry.getValue());
            manager.saveRelation(relatedId, qualifierKey);
        }

        Class<?> childType = PersistenceUtil.getGenericTypes(field).get(1);
        writeCollectionReference(childType, field, entity, values.values());
    }


    private void writeEmptyArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.endArray();
    }

    private void saveManyToManyRelationship(Class<?> genericType, Field field, Object entity, Collection<?> value)
            throws IOException {
        if (value == null) return;

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);

        boolean cascade = isCascadeSave(field.getAnnotation(ManyToMany.class).cascade());
        List<UUID> relatedIds = new ArrayList<>();

        PersistenceContext.registerInContext(entity);

        for (Object item : value) {
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

    private void saveOneToManyRelationship(Class<?> childType, Field currentField, Object parent, Object currentFieldValue) throws IOException {
        PersistenceContext.registerInContext(parent);

        Cascade[] cascade = currentField.getAnnotation(OneToMany.class).cascade();

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

        Class<?> currentType = instance.getClass();
        Field idField = PersistenceUtil.findIdField(currentType);

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            try {
                Field field = findFieldInHierarchy(currentType, name);
                if (field == null) {
                    reader.skipValue();
                    continue;
                }

                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    reader.skipValue();
                    continue;
                }

                field.setAccessible(true);

                if (field.equals(idField)) {
                    instanceId = gson.fromJson(reader, field.getType());
                    field.set(instance, instanceId);
                    PersistenceContext.registerSubContext(currentType, instanceId, instance);
                    continue;
                }

                if (PersistenceUtil.isElementCollection(field.getType())) {
                    readSingleReference(field, instance, instanceId);
                    reader.skipValue();
                } else if (PersistenceUtil.isCollectionOfElementCollection(field)) {
                    Class<?> childType = PersistenceUtil.getGenericType(field);
                    Collection<?> result = readCollectionReference(childType, field, instance, instanceId);
                    field.set(instance, result);
                    reader.skipValue();
                }
                else if(PersistenceUtil.isQualified(field)) {
                    Map<?, ?> result = readQualifiedReference(field, instance, instanceId);
                    field.set(instance, result);
                    reader.skipValue();
                }else {
                    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(field.getGenericType()));
                    field.set(instance, adapter.read(reader));
                }

            } catch (IllegalAccessException e) {
                throw new DeserializationException(
                        "Could not access field " + name + " of class " + currentType.getName(), e);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new DeserializationException(
                        "Could not read qualified reference for field " + name + " of class " + currentType.getName(), e);
            }
        }

        reader.endObject();
        return instance;
    }

    private <K>Map<K, ?> readQualifiedReference(Field field, Object instance, UUID id) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<Class<?>> genericTypes = PersistenceUtil.getGenericTypes(field);
        Collection<?> relatedEntities = readCollectionReference(genericTypes.get(1), field, instance, id);
        Map<K, Object> qualifiedMap = new HashMap<>();
        Qualifier qualifierAnn = field.getAnnotation(Qualifier.class);
        Class<? extends QualifierKeyConverter<K>> converterClass = (Class<? extends QualifierKeyConverter<K>>) qualifierAnn.converter();
        QualifierKeyConverter<K> converter = converterClass.getDeclaredConstructor().newInstance();

        QualifiedReferenceCollectionManager<K> manager = QualifiedReferenceCollectionManagerRegistry.getManager(converter, qualifierAnn.referenceCollectionName());

        for (Object relatedEntity : relatedEntities) {
            UUID relatedId = PersistenceUtil.extractId(relatedEntity);
            List<K> qualifiers = manager.getQualifiers(relatedId);
            for (K qualifier : qualifiers) {
                qualifiedMap.put(qualifier, relatedEntity);
            }
        }
        return qualifiedMap;
    }

    private Collection<?> readCollectionReference(Class<?> childType, Field field, Object instance, UUID id)
            throws IOException, IllegalAccessException {

        if (field.isAnnotationPresent(OneToMany.class)) {
            return readOneToManyRelationship(childType, field, id);
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            return readManyToManyRelationship(childType, field, instance, id);
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

    private Collection<?> readOneToManyRelationship(Class<?> genericType, Field field, UUID instanceId)
            throws IOException {
        if (!field.getAnnotation(OneToMany.class).fetch().equals(Fetch.EAGER)) {
            return Collections.emptySet();
        }

        if (genericType == null) return Collections.emptySet();

        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);
        Collection<Object> children = new HashSet<>();

        List<UUID> childrenIds = ReferenceCollectionManagerRegistry.getManager(type, genericType)
                .getRelatedIds(instanceId, false);

        for (UUID childId : childrenIds) {
            collection.findById(childId).ifPresent(children::add);
        }

        return children;
    }

    private Collection<?> readManyToManyRelationship(Class<?> genericType, Field field, Object instance, UUID id) throws IllegalAccessException, IOException {
        Collection<Object> relatedEntities = new HashSet<>();
        if (!field.getAnnotation(ManyToMany.class).fetch().equals(Fetch.EAGER)) {
            field.set(instance, relatedEntities);
            return relatedEntities;
        }

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

        return relatedEntities;
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
    private Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private boolean isCascadeSave(Cascade[] cascade) {
        return Arrays.stream(cascade).anyMatch(c -> c == Cascade.SAVE);
    }
}
