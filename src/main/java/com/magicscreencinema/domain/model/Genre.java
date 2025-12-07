package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Fetch;
import com.magicscreencinema.persistence.declaration.Id;
import com.magicscreencinema.persistence.declaration.ManyToMany;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "genres")
public class Genre {
    @Id
    private UUID id;
    private String name;

    //--associations
    @ManyToMany(fetch = Fetch.EAGER)
    private Set<Movie> movies;

    //--constructors
    private Genre() {
    }

    public Genre(String name) {
        this.movies = new HashSet<>();
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.id = UUID.randomUUID();
    }

    public Genre(String name, Set<Movie> movies) {
        this(name);

        FieldValidator.validateObjectNotNull(movies, "Movies");
        for (Movie movie : movies) {
            addMovie(movie);
        }
    }

    //--association logic
    //movie
    public void addMovie(Movie movie) {
        FieldValidator.validateObjectNotNull(movie, "Movie");
        this.movies.add(movie);
        movie.addGenre(this);
    }

    public void removeMovie(Movie movie) {
        FieldValidator.validateObjectNotNull(movie, "Movie");

        if (!this.movies.contains(movie)) return;

        this.movies.remove(movie);
        movie.removeGenre(this);
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