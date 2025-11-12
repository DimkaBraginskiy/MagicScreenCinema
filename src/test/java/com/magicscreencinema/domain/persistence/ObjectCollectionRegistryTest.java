package com.magicscreencinema.domain.persistence;

import com.magicscreencinema.domain.persistence.testmodel.PersonAnnotatedWithId;
import com.magicscreencinema.domain.persistence.testmodel.PersonAnnotatedWithoutId;
import com.magicscreencinema.domain.persistence.testmodel.PersonNotAnnotated;
import com.magicscreencinema.persistence.ObjectCollection;
import com.magicscreencinema.persistence.ObjectCollectionRegistry;
import com.magicscreencinema.persistence.exception.MissingIdException;
import com.magicscreencinema.persistence.exception.NotACollectionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectCollectionRegistryTest {
    @Test
    public void getCollection_WithNotElementCollectionAnnotatedClassObject_ShouldThrowNotACollectionException() {
        NotACollectionException exception = assertThrows(NotACollectionException.class, () -> {
            ObjectCollectionRegistry.getCollection(PersonNotAnnotated.class);
        });
        assertEquals(PersonNotAnnotated.class.getName() + " is not annotated with @ElementCollection", exception.getMessage());
    }

    @Test
    public void getCollection_WithElementCollectionAnnotatedClassWithIdObject_ShouldReturnObjectCollectionObject() {
        ObjectCollection<PersonAnnotatedWithId> collection = ObjectCollectionRegistry.getCollection(PersonAnnotatedWithId.class);

        assertNotNull(collection);
    }

    @Test
    public void getCollection_WithElementCollectionAnnotatedClassWithoutIdObject_ShouldThrowMissingIdException() {
        MissingIdException exception = assertThrows(MissingIdException.class, () -> {
            ObjectCollectionRegistry.getCollection(PersonAnnotatedWithoutId.class);
        });
        assertEquals("No field with @Id annotation found in class " + PersonAnnotatedWithoutId.class.getName(), exception.getMessage());
    }
}
