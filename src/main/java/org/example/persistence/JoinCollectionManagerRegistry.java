package org.example.persistence;

import java.util.HashMap;
import java.util.Map;

class JoinCollectionManagerRegistry {
    private static final Map<String, JoinCollectionManager> managers = new HashMap<>();

    static JoinCollectionManager getManager(String tableName) {
        return managers.computeIfAbsent(
                tableName,
                JoinCollectionManager::new
        );
    }
}
