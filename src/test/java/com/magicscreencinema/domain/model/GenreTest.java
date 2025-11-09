package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class GenreTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateGenre() {
        Genre genre = new Genre("Fantasy");
        assertEquals("Fantasy", genre.getName());
    }

    @Test
    public void constructor_WithNullNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Genre(null);
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Genre("");
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void setName_WithNullNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Genre genre = new Genre("Fantasy");
            genre.setName(null);
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void setName_WithEmptyNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Genre genre = new Genre("Fantasy");
            genre.setName("");
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void setName_WithValidParameter_ShouldChangeGenreName() {
        Genre genre = new Genre("Fantasy");
        genre.setName("Horror");

        assertEquals("Horror", genre.getName());
    }
}
