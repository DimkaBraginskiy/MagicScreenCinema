package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.Duration;
import java.time.LocalDateTime;

public class Seance {
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public boolean isCancelled;
    public static int advertisementsTime = 20;

    public Movie movie;
    public Hall hall;

    public Seance(LocalDateTime startTime, boolean isCancelled, Movie movie, Hall hall) {
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        this.endTime = getEndTime();
        this.isCancelled = FieldValidator.validateObjectNotNull(isCancelled, "Is Cancelled");
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
                .plusMinutes(advertisementsTime);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = FieldValidator.validateObjectNotNull(isCancelled, "Is Cancelled");
    }

    public static int getAdvertisementsTime() {
        return advertisementsTime;
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
        this.hall = FieldValidator.validateObjectNotNull(hall, "Hall");;
    }
}