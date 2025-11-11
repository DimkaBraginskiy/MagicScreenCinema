package org.example.persistence;

import org.example.persistence.declaration.ElementCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReferenceCollectionManagerRegistry {
    private static final Map<String, ReferenceCollectionManager> managers = new HashMap<>();

    static ReferenceCollectionManager getManager(Class<?> a, Class<?> b) {
        String collectionName =resolveReferenceCollectionName(a, b);
        return managers.computeIfAbsent(
                collectionName,
                ReferenceCollectionManager::new
        );
    }

    private static String resolveReferenceCollectionName(Class<?> classA, Class<?> classB) {
        String nameA = getCollectionName(classA);
        String nameB = getCollectionName(classB);

        return Stream.of(nameA, nameB)
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.joining("_"));
    }

    private static String getCollectionName(Class<?> clazz) {
        ElementCollection annotation = clazz.getAnnotation(ElementCollection.class);
        if (annotation != null && !annotation.name().isEmpty()) {
            return annotation.name();
        }
        return clazz.getSimpleName().toLowerCase();
    }
}
