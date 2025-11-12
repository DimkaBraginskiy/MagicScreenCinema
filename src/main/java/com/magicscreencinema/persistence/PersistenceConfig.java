package com.magicscreencinema.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        try (InputStream in = PersistenceConfig.class.getClassLoader().getResourceAsStream("persistence.properties")) {
            if (in == null) {
                throw new FileNotFoundException("persistence.properties not found in classpath");
            }
            props.load(in);
        }

        DATABASE_PATH = Path.of(props.getProperty("database.path", "db")).toAbsolutePath();
    }

    static Path resolveCollectionPath(String collectionName) {
        return DATABASE_PATH.resolve(collectionName);
    }
}

