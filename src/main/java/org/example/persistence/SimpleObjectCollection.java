package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SimpleObjectCollection<T> implements ObjectCollection<T>{
    private final Class<T> objectClass;
    private final Field idField;
    private final String collectionName;
    public SimpleObjectCollection(Class<T> objectClass) {
        this.objectClass = objectClass;
        if (!objectClass.isAnnotationPresent(Collection.class)) {
            throw new NotACollectionException("The class " + objectClass.getName() + " is not annotated with @Collection");
        }
        this.idField = PersistenceUtil.findIdField(objectClass).orElseThrow(() ->
                new MissingIdException("The class " + objectClass.getName() + " is missing an @Id annotated field"));
        collectionName = objectClass.getAnnotation(Collection.class).name();
    }
    @Override
    public void save(T object) {
        Object id = PersistenceUtil.extractId(object, idField);

        Gson gson = new GsonBuilder().
                registerTypeAdapterFactory(new ReferenceTypeAdapterFactory())
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(object);

        Path collectionPath = PersistenceConfig.resolveCollectionPath(collectionName);

        Path objectPath = collectionPath.resolve(id + ".json");
        try {
            Files.writeString(objectPath, json);
        } catch (IOException e) {
            throw new CouldNotPersistObjectException("Could not persist object of class " + objectClass.getName(), e);
        }
    }

    @Override
    public Optional<T> findById(Object id) {
        Path objectPath = getObjectFilePath(id);

        if(Files.exists(objectPath)){
            try {
                String json = Files.readString(objectPath);
                Gson gson = new Gson();
                return Optional.ofNullable(gson.fromJson(json, objectClass));
            } catch (IOException e) {
                throw new CouldNotReadObjectException("Could not read object of class " + objectClass.getName() + " with id " + id, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        Path collectionPath = PersistenceConfig.resolveCollectionPath(collectionName);
        File folder = collectionPath.toFile();

        List<T> results = new ArrayList<>();
        for(File file : Objects.requireNonNull(folder.listFiles())){
            if(file.isFile() && file.getName().endsWith(".json")){
                try {
                    String json = Files.readString(file.toPath());
                    Gson gson = new Gson();
                    T object = gson.fromJson(json, objectClass);
                    results.add(object);
                } catch (IOException e) {
                    throw new CouldNotReadObjectException("Could not read object file " + file.getName(), e);
                }
            }
        }
        return results;
    }

    @Override
    public boolean existsById(Object id) {
        Path objectPath = getObjectFilePath(id);
        return Files.exists(objectPath);
    }

    @Override
    public boolean deleteById(Object id) {
        Path objectPath = getObjectFilePath(id);
        try {
            return Files.deleteIfExists(objectPath);
        } catch (IOException e) {
            return false;
        }
    }

    private Path getObjectFilePath(Object id) {
        String idStr = id.toString();
        Path collectionPath = PersistenceConfig.resolveCollectionPath(collectionName);
        return collectionPath.resolve(idStr + ".json");
    }

}
