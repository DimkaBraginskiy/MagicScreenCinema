package org.example.persistence;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersistenceManager {
    public static void run() throws Exception {
        String packageName = PersistenceConfig.getModelPackage();
        Path databasePath = PersistenceConfig.getDatabasePath();

        List<Class<?>> classes = scanClasses(packageName);

        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Collection.class)) {
                Collection coll = cls.getAnnotation(Collection.class);
                String folderName = coll.name();

                Path folderPath = databasePath.resolve(folderName);
                if (!Files.exists(folderPath)) {
                    Files.createDirectories(folderPath);
                    System.out.println("Created collection folder: " + folderPath);
                }
            }
        }
    }

    private static List<Class<?>> scanClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) throw new RuntimeException("Package not found: " + packageName);

        File folder = new File(resource.toURI());
        if (!folder.exists()) return classes;

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);
                classes.add(cls);
            }
        }

        return classes;
    }
}
