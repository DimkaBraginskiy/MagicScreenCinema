package org.example.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObjectCollection<T> {
    void save(T object);
    Optional<T> findById(UUID id, boolean flushContext);
    Optional<T> findById(UUID id);
    List<T> findAll(boolean flushContext);
    List<T> findAll();
    boolean existsById(UUID id);
    boolean deleteById(UUID id);
}
