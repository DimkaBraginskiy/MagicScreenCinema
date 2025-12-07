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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ReservationTest {
    @Test
    public void createCustomerAssociation_WithValidParameter_ShouldCreateReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);

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
    public void createCustomerAssociation_WithNullCustomerParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                    ReservationStatusEnum.PENDING, seance, new HashSet<>(), null);
        });
        assertEquals("Customer can not be null", exception.getMessage());
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

        reservation.assignCustomer(customer);
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
    public void createCustomerAssociation_WithSameCustomer_ShouldNotCreateReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);
        reservation.assignCustomer(customer);

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
    public void createCustomerAssociation_WithDifferentCustomer_ShouldUpdateReverseAssociation() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);
        Customer customer2 = new Customer("Fname2", "Lname2", "2345678", "example@gmail.com",
                "pass2", LocalDate.of(1999, 10, 7), 0);

        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);
        reservation.assignCustomer(customer2);

        ReservationKey key = new ReservationKey(reservation.getReservationNumber(), reservation.getReservationTime());

        assertTrue(customer2.getReservations().containsKey(key));
        assertFalse(customer.getReservations().containsKey(key));
        assertEquals(reservation, customer2.getReservation(key));

        assertEquals(customer2, reservation.getCustomer());
        assertEquals(seance, reservation.getSeance());
        assertEquals(movie, reservation.getSeance().getMovie());
        assertEquals(hall, reservation.getSeance().getHall());
        assertEquals(0, customer.getReservations().size());
        assertEquals(1, customer2.getReservations().size());
    }

    @Test
    public void updateCustomerAssociation_WithNullCustomerParameter_ShouldThrowNullAttributeException() {
        Hall hall = new Hall(20, HallTypeEnum.THREE_D, 20, 2);
        Movie movie = new Movie("Test name", AgeRestrictionEnum.SIX, "description",
                LocalDate.of(2027, 10, 2), 10_1000);
        Seance seance = new Seance(LocalDateTime.of(2026, 10, 2, 12, 0),
                false, movie, hall);

        Customer customer = new Customer("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7), 0);

        Reservation reservation = new Reservation(LocalDateTime.of(2030, 9, 1, 12, 0),
                ReservationStatusEnum.PENDING, seance, new HashSet<>(), customer);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            reservation.assignCustomer(null);
        });
        assertEquals("Customer can not be null", exception.getMessage());
    }
}
