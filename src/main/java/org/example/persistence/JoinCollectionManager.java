package org.example.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

class JoinCollectionManager {
    private final Path path;

    public JoinCollectionManager(String name) {
        path = PersistenceConfig.resolveCollectionPath(name);
    }

    public void saveRelations(UUID ownerId, Iterable<UUID> relatedIds) throws IOException {
        for (UUID relatedId : relatedIds) {
            saveRelation(ownerId, relatedId);
        }
    }

    public void saveRelationsInverse(Iterable<UUID> ownerIds, UUID relatedId) throws IOException {
        for (UUID ownerId : ownerIds) {
            saveRelation(ownerId, relatedId);
        }
    }

    public void saveRelation(UUID ownerId, UUID relatedId) throws IOException {
        String record = ownerId + "_" +relatedId;
        if(existsById(ownerId, relatedId)){
            return;
        }
        Files.writeString(path, record + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<UUID> getRelatedIds(UUID ownerId) throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        var lines = Files.readAllLines(path);
        return lines.stream()
                .filter(line -> line.startsWith(ownerId + "_"))
                .map(line -> line.substring(line.indexOf("_") + 1))
                .map(UUID::fromString)
                .toList();
    }

    public void clearRelation(UUID ownerId) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        var lines = Files.readAllLines(path);
        var filteredLines = lines.stream()
                .filter(line -> !line.startsWith(ownerId + "_"))
                .toList();
        Files.write(path, filteredLines, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void deleteRelation(UUID ownerId, UUID relatedId) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        var lines = Files.readAllLines(path);
        String recordToDelete = ownerId + "_" + relatedId;
        var filteredLines = lines.stream()
                .filter(line -> !line.equals(recordToDelete))
                .toList();
        Files.write(path, filteredLines, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public boolean existsById(UUID ownerId, UUID relatedId) throws IOException {
        if (!Files.exists(path)) {
            return false;
        }
        var lines = Files.readAllLines(path);
        String record = ownerId + "_" + relatedId;
        return lines.stream().anyMatch(line -> line.equals(record));
    }

    public List<UUID> getRelatedIdsInverse(UUID id) {
        try {
            if (!Files.exists(path)) {
                return List.of();
            }
            var lines = Files.readAllLines(path);
            return lines.stream()
                    .filter(line -> line.endsWith("_" + id))
                    .map(line -> line.substring(0, line.indexOf("_")))
                    .map(UUID::fromString)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Could not read join collection file: " + path, e);
        }
    }
}
