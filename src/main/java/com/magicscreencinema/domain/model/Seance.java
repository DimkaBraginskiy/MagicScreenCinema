package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.AlreadyAssignedException;
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
        setHall(hall);
        setMovie(movie);
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
    public void setMovie(Movie newMovie) {
        FieldValidator.validateObjectNotNull(newMovie, "Movie");

        // to prevent stackOverFlow
        if (this.movie == newMovie) return;

        if (this.movie != null) {
            Movie oldMovie = this.movie;
            oldMovie.reassignSeance(newMovie, this);
        }

        this.movie = newMovie;

        newMovie.addSeance(this);
    }

    // hall
    public void setHall(Hall newHall) {
        FieldValidator.validateObjectNotNull(newHall, "Hall");

        // to prevent stackOverFlow
        if (this.hall == newHall) return;

        if (this.hall != null) {
            Hall oldHall = this.hall;
            oldHall.reassignSeance(newHall, this);
        }

        this.hall = newHall;

        newHall.addSeance(this);
    }

    //reservation
    public void addReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");

        // allow if the reservation is already assigned to THIS seance
        if (reservation.getSeance() != null && reservation.getSeance() != this) {
            throw new AlreadyAssignedException("Cannot add Reservation directly if it is already assigned to another Seance. Use Reservation.setSeance() to reassign.");
        }

        // to prevent infinite loops
        if (this.reservations.contains(reservation)) return;

        this.reservations.add(reservation);

        if (reservation.getSeance() != this) {
            reservation.setSeance(this);
        }
    }
    public void removeReservation(Reservation reservation) {
        if (this.reservations.remove(reservation)) {
            if (reservation.getSeance() == this) {
                reservation.deleteSeance();
            }
        }
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