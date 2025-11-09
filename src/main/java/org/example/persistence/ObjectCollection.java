package org.example.persistence;

import java.util.List;

interface ObjectCollection<T> {
    void save(T object);
    T findById(Object id);
    List<T> findAll();
    boolean existsById(Object id);
    boolean deleteById(Object id);
}
