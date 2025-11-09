package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
        this.idField = findIdField(objectClass).orElseThrow(() ->
                new MissingIdException("The class " + objectClass.getName() + " is missing an @Id annotated field"));
        collectionName = objectClass.getAnnotation(Collection.class).name();
    }
    @Override
    public void save(T object) {
        Object id = extractId(object);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
    public T findById(Object id) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public boolean existsById(Object id) {
        return false;
    }

    @Override
    public boolean deleteById(Object id) {
        return false;
    }

    private Object extractId(T object) {
        Object id;
        try {
            id = idField.get(object);
        } catch (IllegalAccessException e) {
            throw new CouldNotPersistObjectException("Could not access id field of class " + objectClass.getName(), e);
        }
        if (id == null) {
            throw new MissingIdException("The object of class " + objectClass.getName() + " has a null id");
        }
        return id;
    }

    private Optional<Field> findIdField(Class<?> objectClass) {
        Class<?> current = objectClass;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return Optional.of(field);
                }
            }
            current = current.getSuperclass();
        }
        return Optional.empty();
    }

}
