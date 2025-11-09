package org.example.persistence;

import java.util.List;
import java.util.Optional;

interface ObjectCollection<T> {
    void save(T object);
    Optional<T> findById(Object id);
    List<T> findAll();
    boolean existsById(Object id);
    boolean deleteById(Object id);
}
