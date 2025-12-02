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
import java.util.HashSet;
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

    @Test
    public void updateSeanceAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        ad.addSeance(seance2);

        //reverse check
        assertTrue(seance2.getAdvertisements().contains(ad));
        assertTrue(seance.getAdvertisements().contains(ad));

        assertTrue(ad.getSeances().contains(seance));
        assertEquals(2, ad.getSeances().size());
    }

    @Test
    public void updateSeanceAssociation_WithSameSeance_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        ad.addSeance(seance1);

        //reverse check
        assertTrue(ad.getSeances().contains(seance1));
        assertEquals(seance1.getAdvertisements().stream().findFirst().get(), ad);
        assertEquals(1, seance1.getAdvertisements().size());
    }

    @Test
    public void updateSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            ad.addSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
    }


    @Test
    public void deleteSeanceAssociation_WithValidParameter_ShouldDeleteReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");
        Advertisement ad2 = new Advertisement("Test", 10, "Test2");

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad, ad2));
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        ad.removeSeance(seance1);

        //reverse check
        assertTrue(seance1.getAdvertisements().contains(ad2));
        assertFalse(ad.getSeances().contains(seance1));
        assertFalse(seance1.getAdvertisements().contains(ad));
        assertTrue(ad.getSeances().contains(seance2));
        assertEquals(1, ad.getSeances().size());
    }

    @Test
    public void deleteSeanceAssociation_WithOneToOneSeanceAdvertisement_ShouldThrowIllegalStateException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ad.removeSeance(seance);
        });
        assertEquals("Cannot remove Seance. Advertisement must have at least one Seance.",
                exception.getMessage());
    }

    @Test
    public void deleteSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            ad.removeSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
        assertTrue(seance.getAdvertisements().contains(ad));
        assertTrue(ad.getSeances().contains(seance));
        assertEquals(1, seance.getAdvertisements().size());
        assertEquals(1, ad.getSeances().size());
    }

    @Test
    public void deleteSeanceAssociation_WithNonExistingSeanceParameter_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        ad.removeSeance(seance2);

        assertTrue(seance.getAdvertisements().contains(ad));
        assertTrue(ad.getSeances().contains(seance));
        assertEquals(1, seance.getAdvertisements().size());
    }
}
