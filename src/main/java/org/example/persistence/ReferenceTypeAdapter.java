package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.example.persistence.exception.DeserializationException;
import org.example.persistence.exception.ReferenceIntegrityException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

class ReferenceTypeAdapter<T> extends TypeAdapter<T> {
    private final Gson gson;
    private final Class<T> type;
    private static final Map<Class<?>, Map<Object, Object>> loadedObjects = new HashMap<>();

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

                if (isCollectionEntity(field.getType())) {
                    if(value == null) continue;

                    Object id = PersistenceUtil.extractId(value);
                    jsonWriter.name(field.getName()).value(id.toString());
                }
                else if (isCollectionEntityCollection(field)) {
                    if(field.isAnnotationPresent(OneToMany.class)) {
                        saveOneToManyChildCollection(field, entityToWrite, value);
                        jsonWriter.name(field.getName());
                        jsonWriter.beginArray();
                        jsonWriter.endArray();
                    }
                }
                else if(value != null){
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

    private void saveOneToManyChildCollection(Field field, Object entityToWrite, Object value) throws IllegalAccessException, NoSuchFieldException {
        boolean isCascadeSave = field.isAnnotationPresent(CascadeSave.class);
        String mappedBy = field.getAnnotation(OneToMany.class).mappedBy();

        Class<?> genType = getGenericType(field);
        Iterable<?> iterable = (Iterable<?>) value;
        if (iterable == null) {
            return;
        }

        for (Object item : iterable) {
            Object childId = PersistenceUtil.extractId(item);

            ObjectCollection<?> childCollection = ObjectCollectionRegistry.getCollection(genType);
            if (!childCollection.existsById(childId) && !isCascadeSave) {
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

        Object id = null;
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

                    loadedObjects.computeIfAbsent(type, k -> new HashMap<>()).put(id, instance);
                    continue;
                }

                if (isCollectionEntity(field.getType())) {
                   readSingleReference(field, instance, reader);
                    ;
                } else if (isCollectionEntityCollection(field)) {
                    if(field.isAnnotationPresent(OneToMany.class)) {
                        readCollectionReference(field, instance, id);
                        reader.skipValue();
                    }
                } else {
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

    public static void flush() {
        loadedObjects.clear();
    }

    private void readSingleReference(Field field, Object instance, JsonReader reader) throws IllegalAccessException, IOException {
        Object refId = gson.fromJson(reader, PersistenceUtil.findIdField(field.getType()).getType());
        Object existing = getFromContext(field.getType(), refId);

        if (existing != null) {
            field.set(instance, existing);
        } else {
            ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
            Object ref = collection.findById(refId).orElse(null);
            if (ref != null) registerInContext(ref);
            field.set(instance, ref);
        }
    }

    private void readCollectionReference(Field field, Object instance, Object id) throws NoSuchFieldException, IllegalAccessException {
        if (!field.isAnnotationPresent(Eager.class)) return;

        Class<?> genericType = getGenericType(field);
        ObjectCollection<?> collectionGeneric = ObjectCollectionRegistry.getCollection(genericType);
        List<Object> children = new ArrayList<>();

        for (Object child : collectionGeneric.findAll()) {
            Field mappedField = genericType.getDeclaredField(field.getAnnotation(OneToMany.class).mappedBy());
            mappedField.setAccessible(true);
            Object parent = mappedField.get(child);
            if (parent != null && PersistenceUtil.extractId(parent).equals(id)) {
                children.add(child);
                registerInContext(child);
            }
        }

        field.set(instance, children);
    }

    private Class<?> getGenericType(Field field) {
        Type parameterType = field.getGenericType();
        if (parameterType instanceof ParameterizedType pType) {
            Type[] actualTypeArguments = pType.getActualTypeArguments();

            if (actualTypeArguments[0] instanceof Class<?> typeArgument) {
                return typeArgument;
            }
        }
        return null;
    }

    private boolean isCollectionEntity(Class<?> cls) {
        return cls.isAnnotationPresent(ElementCollection.class);
    }

    private boolean isCollectionEntityCollection(Field field) {
        if (!Collection.class.isAssignableFrom(field.getType())) return false;
        Type gen = field.getGenericType();
        if (gen instanceof ParameterizedType pt) {
            Type arg = pt.getActualTypeArguments()[0];
            if (arg instanceof Class<?> elementClass) {
                return isCollectionEntity(elementClass);
            }
        }
        return false;
    }

    private void registerInContext(Object obj) {
        try {
            Field idField = PersistenceUtil.findIdField(obj.getClass());
            idField.setAccessible(true);
            Object id = idField.get(obj);
            loadedObjects
                    .computeIfAbsent(obj.getClass(), k -> new HashMap<>())
                    .put(id, obj);
        } catch (Exception ignored) {
        }
    }

    private Object getFromContext(Class<?> clazz, Object id) {
        return loadedObjects.getOrDefault(clazz, Map.of()).get(id);
    }
}
