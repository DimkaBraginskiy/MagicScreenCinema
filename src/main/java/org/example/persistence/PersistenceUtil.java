package org.example.persistence;

import org.example.persistence.exception.MissingIdException;

import java.lang.reflect.Field;

class PersistenceUtil {
    public static Object extractId(Object entity) {
        try {
            var clazz = entity.getClass();
            for (var field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    Object id = field.get(entity);
                    if(id == null){
                        throw new MissingIdException("The object of class " + clazz.getName() + " has a null ID");
                    }
                    return id;
                }
            }
            throw new MissingIdException("No field with @Id annotation found in class " + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access ID field", e);
        }
    }

    public static Object extractId(Object entity, Field idField){
        try {
            idField.setAccessible(true);
            Object id = idField.get(entity);
            if(id == null){
                throw new MissingIdException("The object of class " + entity.getClass().getName() + " has a null ID");
            }
            return id;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access ID field", e);
        }
    }

    public static Field findIdField(Class<?> objectClass) {
        Class<?> current = objectClass;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field;
                }
            }
            current = current.getSuperclass();
        }
        throw new MissingIdException("No field with @Id annotation found in class " + objectClass.getName());
    }

    public static boolean isElementCollection(Class<?> clazz){
        return clazz.isAnnotationPresent(ElementCollection.class);
    }
}
