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
    @Override
    public void save(T object) {
        Class<?> objectClass = object.getClass();
        if (!objectClass.isAnnotationPresent(Collection.class)) {
            throw new NotACollectionException("The class " + objectClass.getName() + " is not annotated with @Collection");
        }
        Object id = extractId(objectClass, object).orElseThrow(() ->
                new MissingIdException("The object of class " + objectClass.getName() + "has the id of value 'null' or is missing an @Id annotated field value"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);

        Path collectionPath = PersistenceConfig.resolveCollectionPath(
                objectClass.getAnnotation(Collection.class).name()
        );

        Path objectPath = collectionPath.resolve(id.toString() + ".json");
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

    private Optional<Object> extractId(Class<?> objectClass, T object) {
        Class<?> current = objectClass;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        return Optional.ofNullable(field.get(object));
                    } catch (IllegalAccessException ignored) {}
                }
            }
            current = current.getSuperclass();
        }
        return Optional.empty();
    }

}
