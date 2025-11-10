package org.example.persistence;

import org.example.persistence.exception.InvalidIdTypeException;
import org.example.persistence.exception.MissingIdException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.UUID;

class PersistenceUtil {
    public static UUID extractId(Object entity) {
        if(entity == null){
            throw new MissingIdException("The provided entity is null");
        }
        var clazz = entity.getClass();
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return extractId(entity, field);
            }
        }
        throw new MissingIdException("No field with @Id annotation found in class " + clazz.getName());
    }

    public static UUID extractId(Object entity, Field idField) {
        try {
            idField.setAccessible(true);
            Object id = idField.get(entity);

            if (id == null) {
                throw new MissingIdException("The object of class " + entity.getClass().getName() + " has a null ID");
            }

            if (id instanceof UUID uuid) {
                return uuid;
            }
            throw new InvalidIdTypeException("The ID field must be of type UUID in class " + entity.getClass().getName());
        }
        catch (IllegalAccessException e) {
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

    public static boolean isElementCollection(Class<?> clazz) {
        return clazz.isAnnotationPresent(ElementCollection.class);
    }

    static Class<?> getGenericType(Field field) {
        Type parameterType = field.getGenericType();
        if (parameterType instanceof ParameterizedType pType) {
            Type[] actualTypeArguments = pType.getActualTypeArguments();

            if (actualTypeArguments[0] instanceof Class<?> typeArgument) {
                return typeArgument;
            }
        }
        return null;
    }

    static boolean isCollectionOfElementCollection(Field field) {
        if (!Collection.class.isAssignableFrom(field.getType())) return false;
        Type gen = field.getGenericType();
        if (gen instanceof ParameterizedType pt) {
            Type arg = pt.getActualTypeArguments()[0];
            if (arg instanceof Class<?> elementClass) {
                return isElementCollection(elementClass);
            }
        }
        return false;
    }
}
