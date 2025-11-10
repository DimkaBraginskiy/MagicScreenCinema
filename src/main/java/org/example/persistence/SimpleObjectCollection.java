package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.persistence.exception.CouldNotPersistObjectException;
import org.example.persistence.exception.CouldNotReadObjectException;
import org.example.persistence.exception.NotACollectionException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class SimpleObjectCollection<T> implements ObjectCollection<T>{
    private final Class<T> objectClass;
    private final Field idField;
    private final String collectionName;
    private final Gson gson;
    public SimpleObjectCollection(Class<T> objectClass) {
        this.objectClass = objectClass;
        if (!objectClass.isAnnotationPresent(ElementCollection.class)) {
            throw new NotACollectionException("The class " + objectClass.getName() + " is not annotated with @Collection");
        }
        this.idField = PersistenceUtil.findIdField(objectClass);
        collectionName = objectClass.getAnnotation(ElementCollection.class).name();
        gson = new GsonBuilder().
                registerTypeAdapterFactory(new ReferenceTypeAdapterFactory())
                .setPrettyPrinting()
                .create();
    }
    @Override
    public void save(T object) {
        Object id = PersistenceUtil.extractId(object, idField);
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
                var result = Optional.ofNullable(gson.fromJson(json, objectClass));
                ReferenceTypeAdapter.flush();
                return result;
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
                    T object = gson.fromJson(json, objectClass);
                    results.add(object);
                    ReferenceTypeAdapter.flush();
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
