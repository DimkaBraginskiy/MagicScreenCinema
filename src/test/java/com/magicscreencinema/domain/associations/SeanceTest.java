package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SeanceTest {
    @Test
    public void creatMovieAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Movie m = seance.getMovie();
        assertEquals("Test name", m.getName());
        assertEquals(AgeRestrictionEnum.SIX, m.getAgeRestriction());
        assertEquals("description", m.getDescription());
        assertEquals(LocalDate.of(2027, 10, 2), m.getPremiereDate());
        assertEquals(10_1000, m.getMovieDuration());

        //reverse check
        assertTrue(movie.getSeances().contains(seance));
        assertEquals(1, movie.getSeances().size());
    }

    @Test
    public void createMovieAssociation_WithNullMovieParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                    false, null, hall);
        });
        assertEquals("Movie can not be null", exception.getMessage());
    }

    @Test
    public void updateMovieAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Movie movie2 = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie2, hall);

        seance2.assignMovie(movie);

        //reverse check
        assertTrue(movie.getSeances().contains(seance1));
        assertFalse(movie2.getSeances().contains(seance2));
        assertTrue(movie.getSeances().contains(seance2));
        assertEquals(seance1.getMovie(), movie);
        assertEquals(seance2.getMovie(), movie);
        assertEquals(2, movie.getSeances().size());
    }

    @Test
    public void updateMovieAssociation_WithSameMovie_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        seance1.assignMovie(movie);

        //reverse check
        assertTrue(movie.getSeances().contains(seance1));
        assertEquals(seance1.getMovie(), movie);
        assertEquals(1, movie.getSeances().size());
    }

    @Test
    public void updateMovieAssociation_WithNullMovieParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            seance.assignMovie(null);
        });
        assertEquals("Movie can not be null", exception.getMessage());
    }

    @Test
    public void creatHallAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Hall h = seance.getHall();
        assertEquals(20, h.getMaxRow());
        assertEquals(HallTypeEnum.THREE_D, h.getHallType());
        assertEquals(20, h.getMaxRow());
        assertEquals(2, h.getRowWidth());

        //Reverse check
        assertTrue(h.getSeances().contains(seance));
        assertEquals(1, h.getSeances().size());
    }

    @Test
    public void createHallAssociation_WithNullHallParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                    false, movie, null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void updateHallAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall1 = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Hall hall2 = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall1);
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall2);

        seance2.assignHall(hall1);

        //reverse check
        assertTrue(hall1.getSeances().contains(seance1));
        assertFalse(hall2.getSeances().contains(seance2));
        assertTrue(hall1.getSeances().contains(seance2));
        assertEquals(seance1.getHall(), hall1);
        assertEquals(seance2.getHall(), hall1);
        assertEquals(2, hall1.getSeances().size());
    }

    @Test
    public void updateHallAssociation_WithSameHall_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        seance1.assignHall(hall);

        //reverse check
        assertTrue(hall.getSeances().contains(seance1));
        assertEquals(seance1.getHall(), hall);
        assertEquals(1, hall.getSeances().size());
    }

    @Test
    public void updateHallAssociation_WithNullHallParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            seance.assignHall(null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void creatAdvertisementAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        Advertisement a = seance.getAdvertisements().stream().findFirst().get();
        assertEquals("Test", a.getName());
        assertEquals(1, a.getSeances().size());
        assertEquals("Test2", a.getAdvertiserName());

        //reverse check
        assertTrue(a.getSeances().contains(seance));
        assertEquals(1, a.getSeances().size());
    }

    @Test
    public void createAdvertisementAssociation_WithNullAdvertisementParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                    false, movie, hall, new HashSet<>(), null);
        });
        assertEquals("Advertisements can not be null", exception.getMessage());
    }

    @Test
    public void updateAdvertisementAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        Advertisement ad2 = new Advertisement("Test", 10, "Test2");
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad2));

        seance2.addAdvertisement(ad);

        //reverse check
        assertTrue(seance2.getAdvertisements().contains(ad2));
        assertTrue(seance2.getAdvertisements().contains(ad));
        assertFalse(seance.getAdvertisements().contains(ad2));
        assertTrue(seance.getAdvertisements().contains(ad));

        assertTrue(ad.getSeances().contains(seance));
        assertEquals(2, ad.getSeances().size());
        assertEquals(1, ad2.getSeances().size());
    }

    @Test
    public void updateAdvertisementAssociation_WithAdvertisement_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        seance1.addAdvertisement(ad);

        //reverse check
        assertTrue(ad.getSeances().contains(seance1));
        assertEquals(seance1.getAdvertisements().stream().findFirst().get(), ad);
        assertEquals(1, seance1.getAdvertisements().size());
    }

    @Test
    public void updateAdvertisementAssociation_WithNullAdvertisementParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            seance.addAdvertisement(null);
        });
        assertEquals("Advertisement can not be null", exception.getMessage());
    }


    @Test
    public void deleteAdvertisementAssociation_WithValidParameter_ShouldDeleteReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad1 = new Advertisement("Test", 10, "Test2");
        Advertisement ad2 = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad1, ad2));
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad1, ad2));

        seance.removeAdvertisement(ad1);

        //reverse check
        assertTrue(seance.getAdvertisements().contains(ad2));
        assertFalse(seance.getAdvertisements().contains(ad1));
        assertTrue(ad1.getSeances().contains(seance2));
        assertEquals(1, seance.getAdvertisements().size());
    }

    @Test
    public void deleteAdvertisementAssociation_WithOneToOneSeanceAdvertisement_ShouldThrowIllegalStateException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            seance.removeAdvertisement(ad);
        });
        assertEquals("Cannot remove Advertisement 'Test'. Seance must have at least one Advertisement.",
                exception.getMessage());
    }

    @Test
    public void deleteAdvertisementAssociation_WithNullAdvertisementParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            seance.removeAdvertisement(null);
        });
        assertEquals("Advertisement can not be null", exception.getMessage());
        assertTrue(seance.getAdvertisements().contains(ad));
        assertTrue(ad.getSeances().contains(seance));
        assertEquals(1, seance.getAdvertisements().size());
        assertEquals(1, ad.getSeances().size());
    }

    @Test
    public void deleteAdvertisementAssociation_WithNonExistingAdvertisementParameter_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Advertisement ad = new Advertisement("Test", 10, "Test2");
        Advertisement ad2 = new Advertisement("Test", 10, "Test2");

        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall, new HashSet<>(), Set.of(ad));

        seance.removeAdvertisement(ad2);

        assertTrue(seance.getAdvertisements().contains(ad));
        assertFalse(seance.getAdvertisements().contains(ad2));
        assertTrue(ad.getSeances().contains(seance));
        assertFalse(ad2.getSeances().contains(seance));
        assertEquals(1, seance.getAdvertisements().size());
    }
}
