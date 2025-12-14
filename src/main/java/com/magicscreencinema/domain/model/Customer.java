package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.InheritanceViolationException;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ElementCollection(name = "customers")
public class Customer{
    private int loyaltyPoints = 0;
    @OneToMany(fetch = Fetch.EAGER)
    @Qualifier(converter = ReservationKeyConverter.class, referenceCollectionName = "reservation_keys")
    private final Map<ReservationKey, Reservation> reservations = new HashMap<>();
    private Person person;

    public Customer(Person person, int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
        this.person = FieldValidator.validateObjectNotNull(person, "Person");

        if(person.getCustomer().isPresent()) {
            throw new InheritanceViolationException("Person is already associated with another Customer.");
        }
    }

    private Customer() {
    }

    //--association logic
    // reservations
    void addReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");
        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());
        reservations.put(key, reservation);
    }

    public Reservation getReservation(ReservationKey key) {
        FieldValidator.validateObjectNotNull(key, "Key");
        return reservations.get(key);
    }

    void removeReservation(Reservation reservation) {
        FieldValidator.validateObjectNotNull(reservation, "Reservation");
        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());
        reservations.remove(key);
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

    public Person getPerson() {
        return person;
    }
}
