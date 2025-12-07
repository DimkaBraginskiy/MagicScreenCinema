package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.Advertisement;
import com.magicscreencinema.domain.model.Hall;
import com.magicscreencinema.domain.model.Movie;
import com.magicscreencinema.domain.model.Seance;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class AdvertisementTest {
    @Test
    public void creatSeanceAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);
        Advertisement ad = new Advertisement("Test", 10, "Test2", Set.of(seance));

        Seance s = ad.getSeances().stream().findFirst().get();
        assertFalse(s.isCancelled());
        assertEquals(movie, s.getMovie());
        assertEquals(hall, s.getHall());

        //reverse check
        assertTrue(ad.getSeances().contains(s));
        assertEquals(1, ad.getSeances().size());
    }

    @Test
    public void createSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Advertisement("Test", 10, "Test2", null);
        });
        assertEquals("Seances can not be null", exception.getMessage());
    }
}
