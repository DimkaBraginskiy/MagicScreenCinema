package com.magicscreencinema.domain.persistence;

import com.magicscreencinema.domain.persistence.testmodel.PersonWithNoArgsConstructor;
import com.magicscreencinema.domain.persistence.testmodel.SimplePerson;
import com.magicscreencinema.persistence.ObjectCollection;
import com.magicscreencinema.persistence.ObjectCollectionRegistry;
import com.magicscreencinema.persistence.exception.CouldNotPersistObjectException;
import com.magicscreencinema.persistence.exception.CouldNotReadObjectException;
import com.magicscreencinema.persistence.exception.MissingNoArgsConstructorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Stream;

public class SimpleObjectCollectionTest {
    @BeforeEach
    void cleanDbFolder() throws IOException {
        Path dbFolder = Paths.get("db-test");

        try (Stream<Path> paths = Files.walk(dbFolder)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        File f = path.toFile();
                        f.setWritable(true);
                        if (!f.delete()) {
                            throw new RuntimeException("Failed to delete: " + path);
                        }
                    });
        }
    }

    @Test
    public void save_WithValidClass_ShouldPersistObject() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        SimplePerson loaded = registry.findById(uuid).get();
        assertNotNull(loaded);
        assertEquals(uuid, person.getId());
        assertEquals("Test", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(2, person.getDays().size());
        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));

        Path filePath = Path.of("db-test/person", uuid + ".json");
        assertTrue(Files.exists(filePath));
    }

    @Test
    public void save_WithNullObject_ShouldNotCreateAnyFiles() {
        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(null);

        Path folder = Path.of("db-test/person");
        try {
            Files.createDirectories(folder);
            try (Stream<Path> files = Files.list(folder)) {
                long count = files.count();
                assertEquals(0, count);
            }
        } catch (IOException _) {
        }
    }

    @Test
    public void save_WithDuplicateObject_ShouldCreateOnlyOneFile() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person);
        registry.save(person);

        try (Stream<Path> files = Files.list(folder)) {
            long count = files.count();
            assertEquals(1, count);
        }

        Path filePath = folder.resolve(uuid + ".json");
        assertTrue(Files.exists(filePath));

        SimplePerson loaded = registry.findById(uuid).get();
        assertNotNull(loaded);
        assertEquals(uuid, person.getId());
        assertEquals("Test", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(0, person.getDays().size());
    }

    @Test
    public void save_WithDuplicateIdObject_ShouldUpdateExistingJSONFile() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person1 = new SimplePerson(uuid, "Test1", 20, List.of(), "test1@email");
        SimplePerson person2 = new SimplePerson(uuid, "Test2", 20, List.of(), "test2@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person1);
        registry.save(person2);

        try (Stream<Path> files = Files.list(folder)) {
            long count = files.count();
            assertEquals(1, count);
        }

        Path filePath = folder.resolve(uuid + ".json");
        assertTrue(Files.exists(filePath));

        SimplePerson loaded = registry.findById(uuid).get();
        assertNotNull(loaded);
        assertEquals(uuid, person2.getId());
        assertEquals("Test2", person2.getName());
        assertEquals(20, person2.getAge());
        assertEquals("test2@email", person2.getEmail().get());
        assertNotNull(person2.getDays());
        assertEquals(0, person2.getDays().size());
    }


    @Test
    public void save_WhenFileNotWritable_ShouldThrowCouldNotPersistObjectException() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);
        Path file = folder.resolve(uuid + ".json");

        Files.createFile(file);
        file.toFile().setWritable(false);

        assertThrows(CouldNotPersistObjectException.class, () -> registry.save(person));
        assertTrue(Files.exists(folder));
        assertFalse(Files.exists(file));

        file.toFile().setWritable(true);
    }

    @Test
    public void findById_WithClassWithoutNoArgsConstructor_ShouldThrowMissingNoArgsConstructorException() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        PersonWithNoArgsConstructor person = new PersonWithNoArgsConstructor(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<PersonWithNoArgsConstructor> registry =
                ObjectCollectionRegistry.getCollection(PersonWithNoArgsConstructor.class);
        registry.save(person);

        MissingNoArgsConstructorException exception = assertThrows(MissingNoArgsConstructorException.class, () -> {
            registry.findById(uuid).get();
        });
        assertEquals("Class " + PersonWithNoArgsConstructor.class.getName() + " must have a public or accessible no-argument constructor",
                exception.getMessage());
    }

    @Test
    public void findById_WithValidClassWithExistingId_ShouldReturnObject() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        SimplePerson loaded = registry.findById(uuid).get();
        assertNotNull(loaded);
        assertEquals(uuid, person.getId());
        assertEquals("Test", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(2, person.getDays().size());
        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));
    }

    @Test
    public void findById_WithValidClassWithNonExistingId_ShouldReturnEmptyOptional() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("test100".getBytes());

        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        Optional<SimplePerson> loaded = registry.findById(uuid2);

        assertTrue(loaded.isEmpty());
    }

    @Test
    public void findById_WhenFileWasRemoved_ShouldReturnNull() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person);

        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        Optional<SimplePerson> result = registry.findById(uuid);
        assertTrue(result.isEmpty());
        assertFalse(Files.exists(filePath));
    }

    @Test
    public void findById_WhenPathIsDirectory_ShouldThrowCouldNotReadObjectException() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        Path folder = Path.of("db-test/person");
        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        Files.createDirectories(folder);

        Files.createDirectories(filePath);

        assertThrows(CouldNotReadObjectException.class, () -> registry.findById(uuid));
    }

    @Test
    public void findById_WithInvalidJsonChanged_ShouldThrowFiledNotFoundException() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Original", 20, List.of(DayOfWeek.MONDAY), "original@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person);

        Path filePath = folder.resolve(uuid + ".json");

        String updatedJson = """
                {
                    "id": "%s",
                    "age": 25,
                    "daysOfWeek": ["MONDAY"],
                    "email": "updated@email"
                }
                """.formatted(uuid);
        Files.writeString(filePath, updatedJson);

        assertNull(registry.findById(uuid).get().getName());
    }

    @Test
    public void getAll_WithValidClass_ShouldReturnObjects() {
        UUID uuid1 = UUID.nameUUIDFromBytes("test1".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("test2".getBytes());

        SimplePerson person1 = new SimplePerson(uuid1, "Test1", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test1@email");
        SimplePerson person2 = new SimplePerson(uuid2, "Test2", 20,
                List.of(DayOfWeek.MONDAY), "test2@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person1);
        registry.save(person2);

        List<SimplePerson> loaded = registry.findAll();
        SimplePerson person = loaded.getFirst();

        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals(uuid1, person.getId());
        assertEquals("Test1", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test1@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(2, person.getDays().size());
        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));
    }

    @Test
    public void getAll_WithValidClassAndFlushContextTrue_ShouldReturnObjects() {
        UUID uuid1 = UUID.nameUUIDFromBytes("test1".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("test2".getBytes());

        SimplePerson person1 = new SimplePerson(uuid1, "Test1", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test1@email");
        SimplePerson person2 = new SimplePerson(uuid2, "Test2", 20,
                List.of(DayOfWeek.MONDAY), "test2@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person1);
        registry.save(person2);

        List<SimplePerson> loaded = registry.findAll(true);
        SimplePerson person = loaded.getFirst();

        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals(uuid1, person.getId());
        assertEquals("Test1", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test1@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(2, person.getDays().size());
        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));
    }

    @Test
    public void getAll_WithValidClassAndFlushContextFalse_ShouldReturnObjects() {
        UUID uuid1 = UUID.nameUUIDFromBytes("test1".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("test2".getBytes());

        SimplePerson person1 = new SimplePerson(uuid1, "Test1", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test1@email");
        SimplePerson person2 = new SimplePerson(uuid2, "Test2", 20,
                List.of(DayOfWeek.MONDAY), "test2@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person1);
        registry.save(person2);

        List<SimplePerson> loaded = registry.findAll(false);
        SimplePerson person = loaded.getFirst();

        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals(uuid1, person.getId());
        assertEquals("Test1", person.getName());
        assertEquals(20, person.getAge());
        assertEquals("test1@email", person.getEmail().get());
        assertNotNull(person.getDays());
        assertEquals(2, person.getDays().size());
        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));
    }

    @Test
    public void existsById_WithExistingId_ShouldReturnTrue() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        boolean result = registry.existsById(uuid);
        assertTrue(result);
    }

    @Test
    public void existsById_WithNonExistingId_ShouldReturnFalse() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("tes102".getBytes());

        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        boolean result = registry.existsById(uuid2);
        assertFalse(result);
    }

    @Test
    public void existsById_WhenFileWasRemoved_ShouldReturnFalse() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person);

        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        assertFalse(registry.existsById(uuid));
        assertFalse(Files.exists(filePath));
    }

    @Test
    public void existsById_WhenPathIsDirectory_ShouldReturnTrue() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        Path folder = Path.of("db-test/person");
        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        Files.createDirectories(folder);

        Files.createDirectories(filePath);

        assertTrue(registry.existsById(uuid));
    }

    @Test
    public void deleteById_WithNonExistingId_ShouldReturnFalse() throws IOException {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("tes102".getBytes());

        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        boolean result = registry.deleteById(uuid2);
        assertFalse(result);
        assertFalse(registry.existsById(uuid2));
    }

    @Test
    public void deleteById_WithExistingId_ShouldReturnFalse() throws IOException {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());

        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        boolean result = registry.deleteById(uuid);
        assertTrue(result);
        assertFalse(registry.existsById(uuid));

        Path filePath = Path.of("db-test/person", uuid + ".json");
        assertFalse(Files.exists(filePath));
    }

    @Test
    public void deleteById_WhenFileWasRemoved_ShouldReturnFalse() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);

        Path folder = Path.of("db-test/person");
        Files.createDirectories(folder);

        registry.save(person);

        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        assertFalse(registry.deleteById(uuid));
        assertFalse(Files.exists(filePath));
    }

    @Test
    public void deleteById_WhenPathIsDirectory_ShouldReturnTrue() throws IOException {
        UUID uuid = UUID.randomUUID();
        SimplePerson person = new SimplePerson(uuid, "Test", 20, List.of(DayOfWeek.MONDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        Path folder = Path.of("db-test/person");
        Path filePath = folder.resolve(uuid + ".json");
        Files.deleteIfExists(filePath);

        Files.createDirectories(folder);

        Files.createDirectories(filePath);

        assertTrue(registry.deleteById(uuid));
        assertTrue(Files.exists(folder));
        assertFalse(Files.exists(filePath));
    }
}
