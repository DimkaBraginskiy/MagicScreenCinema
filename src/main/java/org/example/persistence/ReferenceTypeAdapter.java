package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.example.persistence.exception.DeserializationException;

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
    public void write(JsonWriter jsonWriter, T t) throws IOException {
        jsonWriter.beginObject();
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (field.getType().isAnnotationPresent(ElementCollection.class)) {
                    Object id = PersistenceUtil.extractId(value);
                    jsonWriter.name(field.getName()).value(id.toString());
                }
                else if (Iterable.class.isAssignableFrom(field.getType())) {
                    jsonWriter.name(field.getName());
                    jsonWriter.beginArray();
                    Iterable<?> iterable = (Iterable<?>) value;
                    if (iterable != null) {
                        for (Object item : iterable) {
                            Object id = PersistenceUtil.extractId(item);
                            jsonWriter.value(id.toString());
                        }
                    }
                    jsonWriter.endArray();
                }
                else {
                    if (value != null) {
                        jsonWriter.name(field.getName());
                        gson.toJson(value, field.getType(), jsonWriter);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName(), e);
            }
        }
        jsonWriter.endObject();
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
                System.out.println("Deserializing field: " + field.getName() + " of type: " + field.getType().getName());

                if (field.equals(idField)) {
                    id = gson.fromJson(reader, field.getType());
                    field.set(instance, id);

                    Map<Object, Object> typeMap = new HashMap<>();
                    typeMap.put(id, instance);
                    loadedObjects.put(type, typeMap);
                    continue;
                }

                if (field.getType().isAnnotationPresent(ElementCollection.class)) {
                    System.out.println("Field " + field.getName() + " is a reference to another entity.");
                    Object refId = gson.fromJson(reader, PersistenceUtil.findIdField(field.getType()).getType());
                    Object existing = getFromContext(field.getType(), refId);

                    if (existing != null) {
                        field.set(instance, existing);
                        System.out.println("Found existing instance in context for id: " + refId);
                    } else {
                        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(field.getType());
                        Object ref = collection.findById(refId).orElse(null);
                        if (ref != null) registerInContext(ref);
                        field.set(instance, ref);
                        System.out.println("Loaded instance from collection for id: " + refId);
                    };
                }

                else if (isCollectionEntityCollection(field)) {
                    System.out.println("Field " + field.getName() + " is a collection of entity references.");
                    if (field.isAnnotationPresent(Eager.class)) {
                        Class<?> genericType = getGenericType(field);
                        ObjectCollection<?> collection = ObjectCollectionRegistry.getCollection(genericType);

                        List<Object> list = new ArrayList<>();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            Object elemId = gson.fromJson(reader, PersistenceUtil.findIdField(genericType).getType());
                            System.out.println("Deserializing element with id: " + elemId + " of type: " + genericType.getName());
                            Object existing = getFromContext(genericType, elemId);
                            Object ref = existing != null ? existing : collection.findById(elemId).orElse(null);
                            if (ref != null) registerInContext(ref);
                            list.add(ref);
                        }
                        reader.endArray();
                        field.set(instance, list);
                    } else {
                        reader.skipValue();
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
        loadedObjects.remove(type);
        return instance;
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
        } catch (Exception ignored) {}
    }

    private Object getFromContext(Class<?> clazz, Object id) {
        return loadedObjects.getOrDefault(clazz, Map.of()).get(id);
    }
}
