package org.example.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

class PersistenceConfig {

    private static Path DATABASE_PATH;

    static {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static void load() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("persistence.properties")) {
            props.load(fis);
        }

        DATABASE_PATH = Path.of(props.getProperty("database.path", "db")).toAbsolutePath();
    }

    static Path resolveCollectionPath(String collectionName) {
        return DATABASE_PATH.resolve(collectionName);
    }
}

