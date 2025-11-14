package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.exception.DateInPastException;
import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NonPositiveValueException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateMovie() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);

        assertNotNull(movie);
        assertEquals("Test", movie.getName());
        assertEquals(AgeRestrictionEnum.EIGHTEEN, movie.getAgeRestriction());
        assertEquals("Description", movie.getDescription());
        assertEquals(LocalDate.of(2026, 10, 2), movie.getPremiereDate());
        assertEquals(2_400_000, movie.getMovieDuration());
    }

    @Test
    public void constructor_WithNullNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie(null, AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Movie("", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullAgeRestrictionField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie("Test", null, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
        });
        assertEquals("Age Restriction can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullDescriptionField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, null,
                    LocalDate.of(2026, 10, 2), 2_400_000);
        });
        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyDescriptionField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "",
                    LocalDate.of(2026, 10, 2), 2_400_000);
        });
        assertEquals("Description can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithPastPremiereDateField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2023, 10, 2), 2_400_000);
        });
        assertEquals("Premiere Date can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithNullPremiereDateField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    null, 2_400_000);
        });
        assertEquals("Premiere Date can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeMovieDurationField_ShouldThrowNonPositiveException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), -2_400_000);
        });
        assertEquals("Movie Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroMovieDurationField_ShouldThrowNonPositiveException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 0);
        });
        assertEquals("Movie Duration must be a positive value ( > 0).", exception.getMessage());
    }
    
    @Test
    public void setName_WithNullNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setName(null);
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void setName_WithEmptyNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setName("");
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void setName_WithValidNameParameter_ShouldChangeName() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);
        movie.setName("New name");

        assertEquals("New name", movie.getName());
    }

    @Test
    public void setAgeRestriction_WithNullAgeRestrictionParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setAgeRestriction(null);
        });
        assertEquals("Age Restriction can not be null", exception.getMessage());
    }

    @Test
    public void setAgeRestriction_WithValidAgeRestrictionParameter_ShouldChaneAgeRestriction() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);
        movie.setAgeRestriction(AgeRestrictionEnum.NINE);

        assertEquals(AgeRestrictionEnum.NINE, movie.getAgeRestriction());
    }

    @Test
    public void setDescription_WithNullDescriptionParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setDescription(null);
        });
        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void setDescription_WithEmptyDescriptionParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setDescription("");
        });
        assertEquals("Description can not be empty", exception.getMessage());
    }

    @Test
    public void setDescription_WithValidDescriptionParameter_ShouldChangeDescription() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);
        movie.setDescription("New description");

        assertEquals("New description", movie.getDescription());
    }

    @Test
    public void setPremiereDate_WithPastPremiereDateParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setPremiereDate(LocalDate.of(2023, 10, 1));
        });
        assertEquals("Premiere Date can not be in the past", exception.getMessage());
    }

    @Test
    public void setPremiereDate_WithNullPremiereDateParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setPremiereDate(null);
        });
        assertEquals("Premiere Date can not be null", exception.getMessage());
    }

    @Test
    public void setPremiereDate_WithValidPremiereDateParameter_ShouldChangePremiereDate() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);
        movie.setPremiereDate(LocalDate.of(2027, 10, 2));

        assertEquals(LocalDate.of(2027, 10, 2), movie.getPremiereDate());
    }

    @Test
    public void setMovieDuration_WithNegativeMovieDurationParameter_ShouldThrowNonPositiveException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setMovieDuration(-1);
        });
        assertEquals("Movie Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setMovieDuration_WithZeroMovieDurationParameter_ShouldThrowNonPositiveException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                    LocalDate.of(2026, 10, 2), 2_400_000);
            movie.setMovieDuration(0);
        });
        assertEquals("Movie Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setMovieDuration_WithValidMovieDurationParameter_ShouldChangeMovieDuration() {
        Movie movie = new Movie("Test", AgeRestrictionEnum.EIGHTEEN, "Description",
                LocalDate.of(2026, 10, 2), 2_400_000);
        movie.setMovieDuration(3_000_000);

        assertEquals(3_000_000, movie.getMovieDuration());
    }
}
