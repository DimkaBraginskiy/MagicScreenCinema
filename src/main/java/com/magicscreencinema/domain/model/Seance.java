package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.Duration;
import java.time.LocalDateTime;

public class Seance {
    private LocalDateTime startTime;
    private boolean isCancelled;
    private static final int ADVERTISEMENTS_TIME = 20;

    public Movie movie;
    public Hall hall;

    public Seance(LocalDateTime startTime, boolean isCancelled, Movie movie, Hall hall) {
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        this.isCancelled = isCancelled;
        this.movie = FieldValidator.validateObjectNotNull(movie, "Movie");
        this.hall = FieldValidator.validateObjectNotNull(hall, "Hall");
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
    }

    public LocalDateTime getEndTime() {
        return startTime
                .plus(Duration.ofMillis(movie.getMovieDuration()))
                .plusMinutes(ADVERTISEMENTS_TIME);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public static int getAdvertisementsTime() {
        return ADVERTISEMENTS_TIME;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = FieldValidator.validateObjectNotNull(movie, "Movie");
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = FieldValidator.validateObjectNotNull(hall, "Hall");
    }
}
