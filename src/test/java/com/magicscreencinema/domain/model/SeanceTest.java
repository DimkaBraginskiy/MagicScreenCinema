package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.DateInPastException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SeanceTest {
    private static Hall hall;
    private static Movie movie;

    @BeforeClass
    public static void init() {
        hall = new Hall(1, HallTypeEnum.THREE_D, 12, 10, new ArrayList<>());
        movie = new Movie("Test", AgeRestrictionEnum.SIX, "Description",
                LocalDate.of(2028, 10, 2), 3_000_000);
    }

    @Test
    public void constructor_WithValidParameters_ShouldCreateSeance() {
        Seance seance = new Seance(LocalDateTime.of(2028, 10, 2, 12, 0),
                false, movie, hall);

        assertNotNull(seance);
        assertEquals(LocalDateTime.of(2028, 10, 2, 12, 0), seance.getStartTime());
        assertFalse(seance.isCancelled());
        assertEquals(movie, seance.getMovie());
        assertEquals(hall, seance.getHall());
    }

    @Test
    public void constructor_WithNullStartTimeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(null, false, movie, hall);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithPastStartTimeField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            new Seance(LocalDateTime.of(2003, 10, 2, 12, 0),
                    false, movie, hall);
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithNullMovieField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, null, hall);
        });
        assertEquals("Movie can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullHallField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, movie, null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void setStartTime_WithNullStartTimeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, movie, hall);
            seance.setStartTime(null);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void setStartTime_WithPastStartTimeParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, movie, hall);
            seance.setStartTime(LocalDateTime.of(2003, 10, 2, 12, 0));
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithValidStartTimeParameter_ShouldChangeStartTime() {
        Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                false, movie, hall);
        seance.setStartTime(LocalDateTime.of(2030, 10, 2, 12, 0));

        assertEquals(LocalDateTime.of(2030, 10, 2, 12, 0), seance.getStartTime());
    }

    @Test
    public void setMovie_WithNullMovieParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, movie, hall);
            seance.setMovie(null);
        });
        assertEquals("Movie can not be null", exception.getMessage());
    }

    @Test
    public void setMovie_WithValidMovieParameter_ShouldChangeMovie() {
        Movie movie2 = new Movie("Test1", AgeRestrictionEnum.SIX, "Description1",
                LocalDate.of(2028, 10, 2), 3_000_000);
        Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                false, movie, hall);
        seance.setMovie(movie2);

        assertEquals(movie2, seance.getMovie());
        assertEquals("Test1", seance.getMovie().getName());
        assertEquals("Description1", seance.getMovie().getDescription());
        assertEquals(AgeRestrictionEnum.SIX, seance.getMovie().getAgeRestriction());
        assertEquals(LocalDate.of(2028, 10, 2), seance.getMovie().getPremiereDate());
        assertEquals(3_000_000, seance.getMovie().getMovieDuration(), 0.0001);
    }

    @Test
    public void setHall_WithNullHallParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                    false, movie, hall);
            seance.setHall(null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void setHall_WithValidHallParameter_ShouldChangeHall() {
        Hall hall2 = new Hall(2, HallTypeEnum.THREE_D, 10, 20, new ArrayList<>());
        Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                false, movie, hall);
        seance.setHall(hall2);

        assertEquals(hall2, seance.getHall());
        assertEquals(2, seance.getHall().getHallNumber());
        assertEquals(HallTypeEnum.THREE_D, seance.getHall().getHallType());
        assertEquals(10, seance.getHall().getMaxRow());
        assertEquals(20, seance.getHall().getRowWidth());
        assertNotNull(seance.getHall().getSeats());
    }

    @Test
    public void setIsCancelled_WithValidIsCancelledParameter_ShouldChangeIsCancelled() {
        Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                false, movie, hall);
        seance.setCancelled(true);

        assertTrue(seance.isCancelled());
    }

    @Test
    public void getEndTime_ShouldReturnValidEndTime() {
        Seance seance = new Seance(LocalDateTime.of(2029, 10, 2, 12, 0),
                false, movie, hall);
        LocalDateTime actual = seance.getEndTime();

        assertEquals(LocalDateTime.of(2029, 10, 2, 13, 10), actual);
    }
}
