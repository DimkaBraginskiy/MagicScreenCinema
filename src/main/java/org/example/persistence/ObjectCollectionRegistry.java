package org.example.persistence;

import org.example.persistence.exception.NotACollectionException;

import java.util.HashMap;
import java.util.Map;

public class ObjectCollectionRegistry {
    private static final Map<String, ObjectCollection<?>> collections = new HashMap<>();

    public static <T> void registerCollection(String name, ObjectCollection<T> collection) {
        collections.put(name, collection);
    }

    public static ObjectCollection<?> getCollection(Class<?> clazz) {
        if(clazz.isAnnotationPresent(ElementCollection.class)) {
            ElementCollection annotation = clazz.getAnnotation(ElementCollection.class);
            return collections.get(annotation.name());
        }
        throw new NotACollectionException(clazz.getName() + " is not annotated with @ElementCollection");
    }
}
