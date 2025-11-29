package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "movies")
public class Movie {
    //--basic fields
    @Id
    private UUID id;
    private String name;
    private AgeRestrictionEnum ageRestriction;
    private String description;
    private LocalDate premiereDate;
    private long movieDuration;

    //--associations
    private Set<Seance> seances;
    private Set<Genre> genres;

    //--constructors
    private Movie() {
        seances = new HashSet<>();
    }
    public Movie(String name, AgeRestrictionEnum ageRestriction, String description, LocalDate premiereDate, long movieDuration) {
        this();
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.ageRestriction = FieldValidator.validateObjectNotNull(ageRestriction, "Age Restriction");
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
        this.premiereDate = FieldValidator.validateDateNotInThePast(premiereDate, "Premiere Date");
        this.movieDuration = FieldValidator.validatePositiveNumber(movieDuration, "Movie Duration");
        id = UUID.randomUUID();
    }
    public Movie(String name, AgeRestrictionEnum ageRestriction, String description, LocalDate premiereDate, long movieDuration,
                 Set<Seance> seances, Set<Genre> genres) {
        this(name, ageRestriction, description ,premiereDate, movieDuration);

        FieldValidator.validateObjectNotNull(seances, "seances");
        for (Seance seance : seances){
            addSeance(seance);
        }

        FieldValidator.validateObjectNotNull(genres, "genres");
        for (Genre genre : genres){
            addGenre(genre);
        }
    }

    //--association logic
    //seance
    void addSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.add(seance);
    }
    void removeSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.remove(seance);
    }

    //genre
    public void addGenre(Genre genre) {
        FieldValidator.validateObjectNotNull(genre, "Genre");

        if (this.genres.contains(genre)) return;

        this.genres.add(genre);
        genre.addMovie(this);
    }

    public void removeGenre(Genre genre) {
        FieldValidator.validateObjectNotNull(genre, "Genre");

        if (!this.genres.contains(genre)) return;

        // check because: 1..*
        // we cannot remove the genre if it is the only one left
        if (this.genres.size() <= 1) {
            throw new IllegalStateException("Cannot remove Genre '" + genre.getName() + "'. A Movie must have at least one Genre.");
        }

        this.genres.remove(genre);
        genre.removeMovie(this);
    }

    //--getters
    public Set<Genre> getGenres() {
        return new HashSet<>(genres);
    }
    public Set<Seance> getSeances() {
        return new HashSet<>(seances);
    }
    public String getName() {
        return name;
    }
    public AgeRestrictionEnum getAgeRestriction() {
        return ageRestriction;
    }
    public long getMovieDuration() {
        return movieDuration;
    }
    public LocalDate getPremiereDate() {
        return premiereDate;
    }
    public UUID getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    //--setters
    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }
    public void setAgeRestriction(AgeRestrictionEnum ageRestriction) {
        this.ageRestriction = FieldValidator.validateObjectNotNull(ageRestriction, "Age Restriction");
    }
    public void setDescription(String description) {
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
    }
    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = FieldValidator.validateDateNotInThePast(premiereDate, "Premiere Date");
    }
    public void setMovieDuration(long movieDuration) {
        this.movieDuration = FieldValidator.validatePositiveNumber(movieDuration, "Movie Duration");
    }
}
