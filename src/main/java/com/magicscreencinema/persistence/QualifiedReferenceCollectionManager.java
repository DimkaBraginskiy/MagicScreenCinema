package com.magicscreencinema.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

class QualifiedReferenceCollectionManager<T> {
    private final Path path;
    private final QualifierKeyConverter<T> converter;

    public QualifiedReferenceCollectionManager(String name, QualifierKeyConverter<T> converter) {
        path = PersistenceConfig.resolveCollectionPath(name);
        this.converter = converter;
    }

    public void saveRelation(UUID relatedId, T qualifier) throws IOException {
        String record = converter.encode(qualifier) + "_" + relatedId;
        if (existsByQualifierAndId(qualifier, relatedId)) return;
        Files.writeString(path, record + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<T> getQualifiers(UUID relatedId) throws IOException {
        if (!Files.exists(path)) return List.of();
        var lines = Files.readAllLines(path);
        return lines.stream()
                .filter(line -> line.endsWith("_" + relatedId))
                .map(line -> converter.decode(line.substring(0, line.indexOf("_"))))
                .toList();
    }

    public boolean existsByQualifierAndId(T qualifier, UUID relatedId) throws IOException {
        if (!Files.exists(path)) return false;
        String encoded = converter.encode(qualifier);
        String record = encoded + "_" + relatedId;
        return Files.readAllLines(path).stream().anyMatch(line -> line.equals(record));
    }

    public void clearRelations(UUID relatedId) throws IOException {
        if (!Files.exists(path)) return;
        var lines = Files.readAllLines(path);
        var filtered = lines.stream()
                .filter(line -> !line.endsWith("_" + relatedId))
                .toList();
        Files.write(path, filtered, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

