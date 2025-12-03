package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.enums.ReservationStatusEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void createReservationAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);

        customer.addReservation(reservation);

        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());

        assertTrue(customer.getReservations().containsKey(key));
        assertEquals(reservation, customer.getReservation(key));

        assertEquals(customer, reservation.getCustomer());
        assertEquals(seance, reservation.getSeance());
        assertEquals(movie, reservation.getSeance().getMovie());
        assertEquals(hall, reservation.getSeance().getHall());
        assertEquals(1, customer.getReservations().size());
    }

    @Test
    public void createReservationAssociation_WithNullReservationParameter_ShouldThrowNullAttributeException() {
        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            customer.addReservation(null);
        });
        assertEquals("Reservation can not be null", exception.getMessage());
    }

    @Test
    public void updateReservationAssociation_WithModifiedReservationTime_ShouldChangeReverseKey() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);

        customer.addReservation(reservation);
        reservation.setReservationTime(LocalDateTime.of(2030, 9, 1, 14, 0));

        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());

        assertTrue(customer.getReservations().containsKey(key));
        assertEquals(reservation, customer.getReservation(key));
        assertEquals(14, reservation.getReservationTime().getHour());
        assertEquals(customer, reservation.getCustomer());
        assertEquals(seance, reservation.getSeance());
        assertEquals(movie, reservation.getSeance().getMovie());
        assertEquals(hall, reservation.getSeance().getHall());
        assertEquals(1, customer.getReservations().size());
    }

    @Test
    public void deleteReservationAssociation_WithValidParameter_ShouldDeleteAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);

        customer.removeReservation(reservation);

        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());

        assertFalse(customer.getReservations().containsKey(key));
        assertEquals(customer, reservation.getCustomer());
        assertEquals(0, customer.getReservations().size());
    }

    @Test
    public void deleteReservationAssociation_WithNullReservationParameter_ShouldThrowNullAttributeException() {
        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            customer.removeReservation(null);
        });
        assertEquals("Reservation can not be null", exception.getMessage());
    }
}
