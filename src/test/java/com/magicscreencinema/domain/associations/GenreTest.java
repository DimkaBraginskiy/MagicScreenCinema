package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.Genre;
import com.magicscreencinema.domain.model.Movie;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class GenreTest {
    @Test
    public void createMovieAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie));

        Movie m = genre.getMovies().stream().findFirst().get();
        assertEquals("Test name", m.getName());
        assertEquals(AgeRestrictionEnum.SIX, m.getAgeRestriction());
        assertEquals("description", m.getDescription());
        assertEquals(LocalDate.of(2027, 10, 2), m.getPremiereDate());
        assertEquals(10_1000, m.getMovieDuration());

        //reverse check
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(genre.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void createMovieAssociation_WithNullMovieCollection_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Genre("Test name", null);
        });
        assertEquals("Movies can not be null", exception.getMessage());
    }

    @Test
    public void deleteMovieAssociation_WithValidParameter_ShouldDeleteReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Movie movie2 = new Movie("Test name2", AgeRestrictionEnum.SIX, "description2",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie, movie2));
        Genre genre2 = new Genre("Test genre name2", Set.of(movie2));

        genre.removeMovie(movie2);

        //reverse check
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(movie2.getGenres().contains(genre2));
        assertTrue(genre.getMovies().contains(movie));
        assertTrue(genre2.getMovies().contains(movie2));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void deleteMovieAssociation_WithOneToOneGenreMovie_ShouldThrowIllegalStateException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            genre.removeMovie(movie);
        });
        assertEquals("Cannot remove Genre 'Test genre name'. A Movie must have at least one Genre.",
                exception.getMessage());
    }

    @Test
    public void deleteMovieAssociation_WithNullMovieCollection_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie));

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            genre.removeMovie(null);
        });
        assertEquals("Movie can not be null", exception.getMessage());
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(genre.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void updateMovieAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Genre genre = new Genre("Test genre name", Set.of(movie));
        Genre genre2 = new Genre("Test genre name2");

        genre2.addMovie(movie);

        //reverse check
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(movie.getGenres().contains(genre2));
        assertTrue(genre.getMovies().contains(movie));
        assertTrue(genre2.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, genre2.getMovies().size());
        assertEquals(2, movie.getGenres().size());
    }

    @Test
    public void createMovieAssociation_WithNullMovieParameter_ShouldThrowNullAttributeException() {
        Genre genre = new Genre("Test genre name");

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            genre.addMovie(null);
        });
        assertEquals("Movie can not be null", exception.getMessage());
    }

    @Test
    public void deleteMovieAssociation_WithNonExistingMovieParameter_ShouldNotChangeAssociation() {
        Genre genre = new Genre("Test genre name");
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000,
                new HashSet<>(), Set.of(genre));

        Genre genre2 = new Genre("Test genre name2");
        genre2.removeMovie(movie);

        assertTrue(movie.getGenres().contains(genre));
        assertFalse(movie.getGenres().contains(genre2));
        assertTrue(genre.getMovies().contains(movie));
        assertFalse(genre2.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(0, genre2.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }
}
