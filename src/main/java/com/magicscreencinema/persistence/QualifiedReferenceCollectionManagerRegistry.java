package com.magicscreencinema.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class QualifiedReferenceCollectionManagerRegistry {
    private static final Map<String, QualifiedReferenceCollectionManager<?>> managers = new HashMap<>();
    static <T> QualifiedReferenceCollectionManager<T> getManager(QualifierKeyConverter<T> converter, String collectionName) {
        QualifiedReferenceCollectionManager<T> manager = new QualifiedReferenceCollectionManager<>(collectionName, converter);
        return (QualifiedReferenceCollectionManager<T>) managers.computeIfAbsent(collectionName, _ -> manager);
    }

    static <T> Optional<QualifiedReferenceCollectionManager<T>> getExistingManager(String collectionName) {
        return Optional.ofNullable((QualifiedReferenceCollectionManager<T>) managers.get(collectionName));
    }
}
