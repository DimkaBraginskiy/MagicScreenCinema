package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;
import com.magicscreencinema.persistence.declaration.ManyToOne;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "seance")
public class Seance {
    //--basic fields
    @Id
    private UUID id;
    private LocalDateTime startTime;
    private boolean isCancelled;
    private static final int ADVERTISEMENTS_TIME = 20;

    //--associations
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Hall hall;
    private Set<Reservation> reservations;
    private Set<Advertisement> advertisements;

    //--constructors
    public Seance() {
        this.reservations = new HashSet<>();
        this.advertisements = new HashSet<>();
    }
    public Seance(LocalDateTime startTime, boolean isCancelled, Movie movie, Hall hall) {
        this();
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        this.isCancelled = isCancelled;
        this.reservations = new HashSet<>();
        this.advertisements = new HashSet<>();

        //checks for null in methods!
        assignHall(hall);
        assignMovie(movie);
    }
    public Seance(LocalDateTime startTime, boolean isCancelled, Movie movie, Hall hall,
                  Set<Reservation> reservations, Set<Advertisement> advertisements) {
        this(startTime, isCancelled, movie, hall);

        FieldValidator.validateObjectNotNull(reservations, "reservations");
        for (Reservation reservation : reservations) {
            addReservation(reservation);
        }

        FieldValidator.validateObjectNotNull(advertisements, "advertisements");
        for (Advertisement advertisement : advertisements) {
            addAdvertisement(advertisement);
        }
    }

    //--association logic
    // movie
    public void assignMovie(Movie newMovie) {
        FieldValidator.validateObjectNotNull(newMovie, "Movie");

        // if trying to set the same movie
        if (this.movie == newMovie) return;

        // remove this seance from the old movie's list
        if (this.movie != null) {
            this.movie.removeSeance(this);
        }

        this.movie = newMovie;
        this.movie.addSeance(this);
    }

    // hall
    public void assignHall(Hall newHall) {
        FieldValidator.validateObjectNotNull(newHall, "Hall");

        // if already assigned to this hall
        if (this.hall == newHall) return;

        if (this.hall != null) {
            this.hall.removeSeance(this);
        }

        this.hall = newHall;
        this.hall.addSeance(this);
    }

    //reservation
    void addReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");
        this.reservations.add(reservation);
    }
    void removeReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");
        this.reservations.remove(reservation);
    }

    // advertisement
    public void addAdvertisement(Advertisement advertisement) {
        FieldValidator.validateObjectNotNull(advertisement, "Advertisement");

        if (this.advertisements.contains(advertisement)) return;

        this.advertisements.add(advertisement);
        advertisement.addSeance(this);
    }
    public void removeAdvertisement(Advertisement advertisement) {
        FieldValidator.validateObjectNotNull(advertisement, "Advertisement");

        if (!this.advertisements.contains(advertisement)) return;

        this.advertisements.remove(advertisement);
        advertisement.removeSeance(this);
    }

    //--getters
    public Set<Reservation> getReservations() {
        return new HashSet<>(reservations);
    }
    public Set<Advertisement> getAdvertisements() {
        return new HashSet<>(advertisements);
    }
    public static int getAdvertisementsTime() {
        return ADVERTISEMENTS_TIME;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return startTime
                .plus(Duration.ofMillis(movie.getMovieDuration()))
                .plusMinutes(ADVERTISEMENTS_TIME);
    }
    public Hall getHall() {
        return hall;
    }
    public Movie getMovie() {
        return movie;
    }
    public boolean isCancelled() {
        return isCancelled;
    }

    //--setters
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
    }
}