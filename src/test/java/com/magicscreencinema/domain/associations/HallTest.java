package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.Hall;
import com.magicscreencinema.domain.model.Movie;
import com.magicscreencinema.domain.model.Seance;
import com.magicscreencinema.domain.model.Seat;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

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
    public void createSeatAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Hall hall = new Hall(1, HallTypeEnum.THREE_D, 10, 5);

        Seat seatA = hall.addSeat(1, 1);
        Seat seatB = hall.addSeat(2, 2);

        assertEquals(2, hall.getSeats().size());
        assertEquals(hall, seatA.getHall());
        assertEquals(hall, seatB.getHall());
        assertNotNull(seatA.getHall());
        assertNotNull(seatB.getHall());
    }
}
