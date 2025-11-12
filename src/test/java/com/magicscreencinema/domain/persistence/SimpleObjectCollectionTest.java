package com.magicscreencinema.domain.persistence;

import com.magicscreencinema.domain.persistence.testmodel.PersonWithNoArgsConstructor;
import com.magicscreencinema.domain.persistence.testmodel.SimplePerson;
import com.magicscreencinema.persistence.ObjectCollection;
import com.magicscreencinema.persistence.ObjectCollectionRegistry;
import com.magicscreencinema.persistence.exception.MissingNoArgsConstructorException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.*;

import static org.junit.Assert.*;

public class SimpleObjectCollectionTest {
    private static final Path TEST_DB_PATH = Paths.get("db-test");

    @Test
    public void getById_WithClassWithoutNoArgsConstructor_ShouldThrowMissingNoArgsConstructorException() {
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
    public void getById_WithValidClassWithExistingId_ShouldReturnObject() {
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
    public void getById_WithValidClassWithNonExistingId_ShouldReturnEmptyOptional() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("test100".getBytes());

        SimplePerson person = new SimplePerson(uuid, "Test", 20,
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test@email");

        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
        registry.save(person);

        Optional<SimplePerson> loaded = registry.findById(uuid2);

        assertTrue(loaded.isEmpty());
    }

//    @Test
//    public void getAll_WithValidClass_ShouldReturnObjects() {
//        UUID uuid1 = UUID.nameUUIDFromBytes("test1".getBytes());
//        UUID uuid2 = UUID.nameUUIDFromBytes("test2".getBytes());
//
//        SimplePerson person1 = new SimplePerson(uuid1, "Test1", 20,
//                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "test1@email");
//        SimplePerson person2 = new SimplePerson(uuid2, "Test2", 20,
//                List.of(DayOfWeek.MONDAY), "test2@email");
//
//        ObjectCollection<SimplePerson> registry = ObjectCollectionRegistry.getCollection(SimplePerson.class);
//        registry.save(person1);
//        registry.save(person2);
//
//        List<SimplePerson> loaded = registry.findAll();
//        SimplePerson person = loaded.getFirst();
//
//        assertNotNull(loaded);
//        assertEquals(2, loaded.size());
//        assertEquals(uuid1, person.getId());
//        assertEquals("Test", person.getName());
//        assertEquals(20, person.getAge());
//        assertEquals("test@email", person.getEmail().get());
//        assertNotNull(person.getDays());
//        assertEquals(2, person.getDays().size());
//        assertTrue(person.getDays().contains(DayOfWeek.MONDAY));
//        assertTrue(person.getDays().contains(DayOfWeek.WEDNESDAY));
//    }

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
}
