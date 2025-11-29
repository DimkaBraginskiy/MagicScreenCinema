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
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        this.isCancelled = isCancelled;
        this.reservations = new HashSet<>();
        this.advertisements = new HashSet<>();

        setHall(hall);
        //addMovie(movie);
    }

    public Seance(LocalDateTime startTime, boolean isCancelled, Movie movie, Hall hall,
                  Set<Reservation> reservations, Set<Advertisement> advertisements) {
        this(startTime, isCancelled, movie, hall);

        FieldValidator.validateObjectNotNull(reservations, "reservations");
        for (Reservation reservation : reservations) {
            addReservation(reservation);
        }

//      FieldValidator.validateObjectNotNull(advertisements, "advertisements");
        /*for(Advertisement advertisement : advertisements) {
            addAdvertisement(advertisement);
        }*/
    }

    //--association logic
    //hall
    public void setHall(Hall newHall) {
        FieldValidator.validateObjectNotNull(newHall, "Hall");

        // do nothing if assigning the same hall
        if (this.hall == newHall) return;

        // if the seance already has a hall
        if (this.hall != null) {
            Hall oldHall = this.hall;
            this.hall = null;
            oldHall.reassignSeance(newHall, this);

            this.hall = newHall;

        } else {
            if (!newHall.getSeances().contains(this))
                newHall.addSeance(this);
            this.hall = newHall;
        }
    }

    //reservation
    public void addReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");

        if (reservation.getSeance() != null) {
            throw new AlreadyAssignedException("Cannot add Reservation directly if it is already assigned to another Seance. Use Reservation.setSeance() to reassign.");
        }

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