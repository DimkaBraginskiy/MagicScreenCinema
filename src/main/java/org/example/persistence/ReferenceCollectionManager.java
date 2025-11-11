package org.example.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReferenceCollectionManager {
    private final Path path;

    public ReferenceCollectionManager(String name) {
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

    public List<UUID> getRelatedIds(UUID id, boolean isOwner) throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        var lines = Files.readAllLines(path);
        Predicate<String> filterPredicate = isOwner
                ? line -> line.startsWith(id + "_")
                : line -> line.endsWith("_" + id);

        Function<String, UUID> mapFunction = isOwner
                ? line -> UUID.fromString(line.substring(line.indexOf("_") + 1))
                : line -> UUID.fromString(line.substring(0, line.indexOf("_")));

        return lines.stream()
                .filter(filterPredicate)
                .map(mapFunction)
                .toList();
    }

    private Predicate<String> getFilterPredicate(UUID id, boolean isOwner) {
        return  isOwner
                ? line -> line.startsWith(id + "_")
                : line -> line.endsWith("_" + id);
    }

    public void clearRelation(UUID id, boolean isOwner) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        var lines = Files.readAllLines(path);
        var filteredLines = lines.stream()
                .filter(getFilterPredicate(id, isOwner).negate())
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
}
