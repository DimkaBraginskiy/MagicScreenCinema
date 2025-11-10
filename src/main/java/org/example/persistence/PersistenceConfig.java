package org.example.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

class PersistenceConfig {

    private static Path DATABASE_PATH;
    private static String MODEL_PACKAGE;

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
        MODEL_PACKAGE = props.getProperty("model.package", "com.example.models");
    }

    public static Path getDatabasePath() {
        return DATABASE_PATH;
    }

    public static String getModelPackage() {
        return MODEL_PACKAGE;
    }

    public static Path resolveCollectionPath(String collectionName) {
        return DATABASE_PATH.resolve(collectionName);
    }
}

