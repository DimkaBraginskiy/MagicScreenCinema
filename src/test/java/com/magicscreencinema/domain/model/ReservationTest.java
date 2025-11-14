package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.enums.ReservationStatusEnum;
import com.magicscreencinema.domain.exception.DateInPastException;
import com.magicscreencinema.domain.exception.EmptySeatListException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReservationTest {
    private static Hall hall;
    private static Seat seat;
    private static Discount discount;

    @BeforeClass
    public static void init() {
        hall = new Hall(1, HallTypeEnum.THREE_D, 12, 10, new ArrayList<>());
        seat = new Seat(2, 2, hall);
        hall.setSeats(List.of(seat));
        discount = new Discount(0.3, "code");
    }

    @Test
    public void constructor_WithValidParameters_ShouldCreateReservation() {
        Reservation reservation = new Reservation(LocalDateTime.of(2026, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));

        //Reservation
        assertEquals(LocalDateTime.of(2026, 10, 2, 12, 0), reservation.getReservationTime());
        assertEquals(ReservationStatusEnum.PENDING, reservation.getStatus());

        // Discount
        assertNotNull(reservation.getDiscount());
        assertEquals(0.3, reservation.getDiscount().get().getDiscountAmount(), 0.0001);
        assertEquals("code", reservation.getDiscount().get().getPromoCode());

        // Seat
        assertNotNull(reservation.getSeats());
        assertEquals(1, reservation.getSeats().size());
        Seat reservedSeat = reservation.getSeats().getFirst();
        assertEquals(2, reservedSeat.getRow());
        assertEquals(2, reservedSeat.getRow());
        assertEquals(hall, reservedSeat.getHall());

        // Hall
        assertEquals(1, hall.getHallNumber());
        assertEquals(HallTypeEnum.THREE_D, hall.getHallType());
        assertEquals(12, hall.getMaxRow());
        assertEquals(10, hall.getRowWidth());
        assertEquals(List.of(seat), hall.getSeats());
    }

    @Test
    public void constructor_WithValidParametersAndNullDiscountField_ShouldCreateReservation() {
        Reservation reservation = new Reservation(LocalDateTime.of(2026, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, null, List.of(seat));

        //Reservation
        assertEquals(LocalDateTime.of(2026, 10, 2, 12, 0), reservation.getReservationTime());
        assertEquals(ReservationStatusEnum.PENDING, reservation.getStatus());

        // Discount
        assertTrue(reservation.getDiscount().isEmpty());

        // Seat
        assertNotNull(reservation.getSeats());
        assertEquals(1, reservation.getSeats().size());
        Seat reservedSeat = reservation.getSeats().getFirst();
        assertEquals(2, reservedSeat.getRow());
        assertEquals(2, reservedSeat.getRow());
        assertEquals(hall, reservedSeat.getHall());

        // Hall
        assertEquals(1, hall.getHallNumber());
        assertEquals(HallTypeEnum.THREE_D, hall.getHallType());
        assertEquals(12, hall.getMaxRow());
        assertEquals(10, hall.getRowWidth());
        assertEquals(List.of(seat), hall.getSeats());
    }

    @Test
    public void constructor_WithNullReservationTimeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Reservation(null, ReservationStatusEnum.PENDING, discount, List.of(seat));
        });
        assertEquals("Reservation Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithPastReservationTimeField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            new Reservation(LocalDateTime.of(2003, 10, 3, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
        });
        assertEquals("Reservation Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithNullReservationStatusField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Reservation(LocalDateTime.of(2028, 10, 3, 12, 0),
                    null, discount, List.of(seat));
        });
        assertEquals("Status can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullSeatListField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Reservation(LocalDateTime.of(2026, 12, 3, 12, 0),
                    ReservationStatusEnum.PENDING, discount, null);
        });
        assertEquals("Seats can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptySeatListField_ShouldThrowEmptySeatListException() {
        EmptySeatListException exception = assertThrows(EmptySeatListException.class, () -> {
            new Reservation(LocalDateTime.of(2026, 12, 3, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of());
        });
        assertEquals("Seats can not be empty", exception.getMessage());
    }

    @Test
    public void setReservationTime_WithNullReservationTimeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
            reservation.setReservationTime(null);
        });
        assertEquals("Reservation Time can not be null", exception.getMessage());
    }

    @Test
    public void setReservationTime_WithPastReservationTimeParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
            reservation.setReservationTime(LocalDateTime.of(2001, 10, 2, 12, 0));
        });
        assertEquals("Reservation Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setReservationYTime_WithValidReservationTimeParameter_ShouldChangeReservationTime() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        reservation.setReservationTime(LocalDateTime.of(2030, 10, 2, 12, 0));

        assertEquals(LocalDateTime.of(2030, 10, 2, 12, 0), reservation.getReservationTime());
    }

    @Test
    public void setReservationStatus_WithNullReservationStatusField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
            reservation.setStatus(null);
        });
        assertEquals("Status can not be null", exception.getMessage());
    }

    @Test
    public void setReservationStatus_WithValidReservationStatusParameter_ShouldChangeReservationStatus() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        reservation.setStatus(ReservationStatusEnum.COMPLETED);

        assertEquals(ReservationStatusEnum.COMPLETED, reservation.getStatus());
    }

    @Test
    public void setSeats_WithNullSeatListParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
            reservation.setSeats(null);
        });
        assertEquals("Seats can not be null", exception.getMessage());
    }

    @Test
    public void setSeats_WithEmptySeatListParameter_ShouldThrowEmptySeatListException() {
        EmptySeatListException exception = assertThrows(EmptySeatListException.class, () -> {
            Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                    ReservationStatusEnum.PENDING, discount, List.of(seat));
            reservation.setSeats(List.of());
        });
        assertEquals("Seats can not be empty", exception.getMessage());
    }

    @Test
    public void setSeats_WithValidSeatListParameter_ShouldChangeSeatList() {
        Seat seat2 = new Seat(4, 4, hall);
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        reservation.setSeats(List.of(seat2));

        assertNotNull(reservation.getSeats());
        assertEquals(1, reservation.getSeats().size());
        Seat reservedSeat = reservation.getSeats().getFirst();
        assertEquals(4, reservedSeat.getRow());
        assertEquals(4, reservedSeat.getRow());
        assertEquals(hall, reservedSeat.getHall());
    }

    @Test
    public void setDiscount_WithNullDiscountParameter_ShouldChangeDiscount() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        reservation.setDiscount(null);

        assertTrue(reservation.getDiscount().isEmpty());
    }

    @Test
    public void setDiscount_WithValidDiscountParameter_ShouldChangeDiscount() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        reservation.setDiscount(new Discount(0.6, "new code"));

        assertNotNull(reservation.getDiscount().get());
        assertEquals(0.6, reservation.getDiscount().get().getDiscountAmount(), 0.0001);
        assertEquals("new code", reservation.getDiscount().get().getPromoCode());
    }

    @Test
    public void getTotalPrice_WithAppliedDiscount_ShouldReturnTotalPrice() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, discount, List.of(seat));
        double actual = reservation.getTotalPrice();

        assertEquals(9.8, actual, 0.0001);
    }

    @Test
    public void getTotalPrice_WithoutAppliedDiscount_ShouldReturnTotalPrice() {
        Reservation reservation = new Reservation(LocalDateTime.of(2028, 10, 2, 12, 0),
                ReservationStatusEnum.PENDING, null, List.of(seat));
        double actual = reservation.getTotalPrice();

        assertEquals(14, actual, 0.0001);
    }
}
