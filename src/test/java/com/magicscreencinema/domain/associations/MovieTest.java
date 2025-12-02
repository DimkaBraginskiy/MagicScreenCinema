package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.Genre;
import com.magicscreencinema.domain.model.Hall;
import com.magicscreencinema.domain.model.Movie;
import com.magicscreencinema.domain.model.Seance;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void creatGenreAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Genre genre = new Genre("Test genre name");
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000,
                new HashSet<>(), Set.of(genre));


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
    public void createMovieAssociation_WithNullGenreCollection_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                    LocalDate.of(2027, 10, 2), 10_1000,
                    new HashSet<>(), null);
        });
        assertEquals("Genres can not be null", exception.getMessage());
    }

    @Test
    public void deleteGenreAssociation_WithValidParameter_ShouldDeleteReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Movie movie2 = new Movie("Test name2", AgeRestrictionEnum.SIX, "description2",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie, movie2));
        Genre genre2 = new Genre("Test genre name2", Set.of(movie2));

        movie2.removeGenre(genre);

        //reverse check
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(movie2.getGenres().contains(genre2));
        assertTrue(genre.getMovies().contains(movie));
        assertTrue(genre2.getMovies().contains(movie2));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void deleteGenreAssociation_WithOneToOneGenreMovie_ShouldThrowIllegalStateException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            movie.removeGenre(genre);
        });
        assertEquals("Cannot remove Genre 'Test genre name'. A Movie must have at least one Genre.",
                exception.getMessage());
    }

    @Test
    public void deleteGenreAssociation_WithNullGenreCollection_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Genre genre = new Genre("Test genre name", Set.of(movie));

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            movie.removeGenre(null);
        });
        assertEquals("Genre can not be null", exception.getMessage());
        assertTrue(movie.getGenres().contains(genre));
        assertTrue(genre.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void updateGenreAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Genre genre = new Genre("Test genre name", Set.of(movie));
        Genre genre2 = new Genre("Test genre name2");

        movie.addGenre(genre2);

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
    public void createGenreAssociation_WithNullGenreParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            movie.addGenre(null);
        });
        assertEquals("Genre can not be null", exception.getMessage());
    }

    @Test
    public void deleteGenreAssociation_WithNonExistingGenreParameter_ShouldNotChangeAssociation() {
        Genre genre = new Genre("Test genre name");
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000,
                new HashSet<>(), Set.of(genre));

        Genre genre2 = new Genre("Test genre name2");
        movie.removeGenre(genre2);

        assertTrue(movie.getGenres().contains(genre));
        assertFalse(movie.getGenres().contains(genre2));
        assertTrue(genre.getMovies().contains(movie));
        assertFalse(genre2.getMovies().contains(movie));
        assertEquals(1, genre.getMovies().size());
        assertEquals(0, genre2.getMovies().size());
        assertEquals(1, movie.getGenres().size());
    }

    @Test
    public void creatSeanceAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        movie.addSeance(seance);


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
    public void createSeanceAssociation_WithNullSeanceCollection_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                    LocalDate.of(2027, 10, 2), 10_1000,
                    null, new HashSet<>());
        });
        assertEquals("Seances can not be null", exception.getMessage());
    }

    @Test
    public void deleteSeanceAssociation_WithOneToOneSeanceMovie_ShouldDeleteSeanceFromMovie() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);


        movie.removeSeance(seance1);
        assertFalse(movie.getSeances().contains(seance1));
        assertEquals(0, movie.getSeances().size());
    }

    @Test
    public void deleteSeanceAssociation_WithNullSeanceCollection_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            movie.removeSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
        assertEquals(0, movie.getSeances().size());
    }

    @Test
    public void updateSeanceAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        movie.addSeance(seance1);
        movie.addSeance(seance2);

        //reverse check
        assertTrue(movie.getSeances().contains(seance1));
        assertTrue(movie.getSeances().contains(seance2));
        assertEquals(seance1.getMovie(), movie);
        assertEquals(seance2.getMovie(), movie);
        assertEquals(2, movie.getSeances().size());
    }

    @Test
    public void createSeanceAssociation_WithNullSeanceParameter_ShouldThrowNullAttributeException() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            movie.addSeance(null);
        });
        assertEquals("Seance can not be null", exception.getMessage());
    }

    @Test
    public void deleteSeanceAssociation_WithNonExistingSeanceParameter_ShouldNotChangeAssociation() {
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Movie movie2 = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);

        Seance seance1 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);
        Seance seance2 = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie2, hall);

        movie.removeSeance(seance2);

        assertTrue(movie.getSeances().contains(seance1));
        assertFalse(movie.getSeances().contains(seance2));
        assertEquals(seance1.getMovie(), movie);
        assertNotEquals(seance1.getMovie(), movie2);
        assertEquals(1, movie.getSeances().size());
        assertEquals(1, movie2.getSeances().size());
    }
}
