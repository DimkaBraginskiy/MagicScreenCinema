package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.Hall;
import com.magicscreencinema.domain.model.Movie;
import com.magicscreencinema.domain.model.Seance;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class HallTest {
    @Test
    public void createSeanceAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Seance s = hall.getSeances().stream().findFirst().get();
        assertFalse(s.isCancelled());
        assertEquals(movie, s.getMovie());
        assertEquals(hall, s.getHall());

        //Reverse check
        assertTrue(hall.getSeances().contains(seance));
        assertEquals(1, hall.getSeances().size());
    }

    @Test
    public void createSeanceAssociation_WithNullSeanceCollection_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Hall(20, HallTypeEnum.THREE_D, 20, 2, null);
        });
        assertEquals("Seances can not be null", exception.getMessage());
    }

    @Test
    public void deleteSeanceAssociation_WithValidParameter_ShouldDeleteReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        hall.removeSeance(seance);

        //reverse check
        assertFalse(hall.getSeances().contains(seance));
        assertEquals(0, hall.getSeances().size());
    }

    @Test
    public void deleteSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            hall.removeSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
        assertTrue(hall.getSeances().isEmpty());
    }

    @Test
    public void createSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            hall.addSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
    }

    @Test
    public void deleteSeanceAssociation_WithNonExistingSeanceParameter_ShouldNotChangeAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Hall hall2 = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall2);

        hall.removeSeance(seance2);

        assertTrue(hall.getSeances().contains(seance));
        assertFalse(hall.getSeances().contains(seance2));
        assertEquals(hall, seance.getHall());
        assertEquals(hall2, seance2.getHall());
    }
}
