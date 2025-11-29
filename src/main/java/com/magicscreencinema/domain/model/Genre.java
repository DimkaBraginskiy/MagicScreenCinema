package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "genres")
public class Genre {
    @Id
    private UUID id;
    private String name;

    //--associations
    private Set<Movie> movies;

    //--constructors
    private Genre() {
        this.movies = new HashSet<>();
    }
    public Genre(String name) {
        this();
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.id = UUID.randomUUID();
    }
    public Genre(UUID id, String name, Set<Movie> movies) {
        this(name);

        FieldValidator.validateObjectNotNull(movies, "movies");
        for (Movie movie : movies) {
            addMovie(movie);
        }
    }

    //--association logic
    //movie
    public void addMovie(Movie movie) {
        FieldValidator.validateObjectNotNull(movie, "Movie");
        this.movies.add(movie);
    }
    public void removeMovie(Movie movie) {
        FieldValidator.validateObjectNotNull(movie, "Movie");
        this.movies.remove(movie);
    }

    //--getters
    public Set<Movie> getMovies() {
        return new HashSet<>(movies);
    }
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    //--setters
    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }
}