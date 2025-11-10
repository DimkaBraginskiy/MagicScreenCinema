package org.example.persistence;

import org.example.persistence.exception.NotACollectionException;

import java.util.HashMap;
import java.util.Map;

public class ObjectCollectionRegistry {
    private static final Map<Class<?>, ObjectCollection<?>> collections = new HashMap<>();

    private static <T> SimpleObjectCollection<T> create(Class<T> clazz) {
        return new SimpleObjectCollection<>(clazz);
    }

    public static <T> ObjectCollection<T> getCollection(Class<T> clazz) {
        if(PersistenceUtil.isElementCollection(clazz)) {
            ObjectCollection<?> collection = collections.computeIfAbsent(
                    clazz, k -> create(clazz)
            );
            return (ObjectCollection<T>) collection;
        }
        throw new NotACollectionException(clazz.getName() + " is not annotated with @ElementCollection");
    }
}
