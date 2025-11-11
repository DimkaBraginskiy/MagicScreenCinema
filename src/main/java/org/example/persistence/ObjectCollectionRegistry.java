package org.example.persistence;

import org.example.persistence.exception.NotACollectionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectCollectionRegistry {
    private static final Map<Class<?>, ObjectCollection<?>> collections = new HashMap<>();

    private static <T> SimpleObjectCollection<T> create(Class<T> clazz) {
        return new SimpleObjectCollection<>(clazz);
    }

    public static <T> ObjectCollection<T> getCollection(Class<T> clazz) {
        if(PersistenceUtil.isElementCollection(clazz)) {
            addToDb(clazz);
            ObjectCollection<?> collection = collections.computeIfAbsent(clazz, k -> create(clazz));

            return (ObjectCollection<T>) collection;
        }
        throw new NotACollectionException(clazz.getName() + " is not annotated with @ElementCollection");
    }

    private static void addToDb(Class<?> clazz) {
        ElementCollection coll = clazz.getAnnotation(ElementCollection.class);
        String collectionName = coll.name();
        Path folderPath = PersistenceConfig.resolveCollectionPath(collectionName);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create collection folder: " + folderPath, e);
            }
            System.out.println("Created collection folder: " + folderPath);
        }
    }

    static Set<Class<?>> getRegisteredClasses() {
        return collections.keySet();
    }
}
