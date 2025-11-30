package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ElementCollection(name = "customers")
public class Customer extends Person {
    private int loyaltyPoints = 0;

    private final Map<ReservationKey, Reservation> reservations = new HashMap<>();

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, int loyaltyPoints) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
    }
    private Customer() {
    }

    //--association logic
    // reservations
    public void addReservation(Reservation reservation){
        FieldValidator.validateObjectNotNull(reservation, "reservation");
        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());
        reservations.put(key, reservation);
    }

    public Reservation getReservation(ReservationKey key){
        FieldValidator.validateObjectNotNull(key, "key");
        return reservations.get(key);
    }

    public Map<ReservationKey, Reservation> getReservations() {
        return new HashMap<>(reservations);
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
