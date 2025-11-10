package org.example.persistence;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersistenceContext {
    private final static Map<Class<?>, Map<UUID, Object>> loadedObjects = new HashMap<>();
    static void registerInContext(Object obj) {
        try {
            Field idField = PersistenceUtil.findIdField(obj.getClass());
            idField.setAccessible(true);
            UUID id = (UUID) idField.get(obj);
            loadedObjects
                    .computeIfAbsent(obj.getClass(), k -> new HashMap<>())
                    .put(id, obj);
        } catch (Exception ignored) {
        }
    }

    static void registerSubContext(Class<?> clazz, UUID id, Object obj) {
        loadedObjects
                .computeIfAbsent(clazz, k -> new HashMap<>())
                .put(id, obj);
    }

    static Object getFromContext(Class<?> clazz, UUID id) {
        return loadedObjects.getOrDefault(clazz, Map.of()).get(id);
    }

    static void flush() {
        loadedObjects.clear();
    }
}
