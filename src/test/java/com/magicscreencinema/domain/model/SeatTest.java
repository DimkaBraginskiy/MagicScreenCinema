package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.InvalidRowException;
import com.magicscreencinema.domain.exception.NonPositiveValueException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SeatTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateSeat() {
        Seat seat = new Seat(1, 1,
                new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));

        assertNotNull(seat);
        assertEquals(1, seat.getRow());
        assertEquals(1, seat.getRow());
        assertNotNull(seat.getHall());
    }

    @Test
    public void constructor_WithNegativeSeatNumberField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Seat(-2, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        });
        assertEquals("Seat Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroSeatNumberField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Seat(0, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        });
        assertEquals("Seat Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeRowField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Seat(1, -1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        });
        assertEquals("Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroRowField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Seat(1, 0,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        });
        assertEquals("Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidRowField_ShouldThrowInvalidRowException() {
        InvalidRowException exception = assertThrows(InvalidRowException.class, () -> {
            new Seat(1, 40,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        });
        assertEquals("Seat row exceeds hall max rows.", exception.getMessage());
    }

    @Test
    public void constructor_WithNullHallField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Seat(1, 40, null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void setSeatNumber_WithNegativeSeatNumberParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Seat seat = new Seat(1, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setSeatNumber(-2);
        });
        assertEquals("Seat Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setSeatNumber_WithZeroSeatNumberParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Seat seat = new Seat(1, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setSeatNumber(0);
        });
        assertEquals("Seat Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setRow_WithNegativeRowParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Seat seat = new Seat(1, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setRow(-2);
        });
        assertEquals("Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setRow_WithZeroRowParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Seat seat = new Seat(1, 0,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setRow(0);
        });
        assertEquals("Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setRow_WithInvalidRowParameter_ShouldThrowInvalidRowException() {
        InvalidRowException exception = assertThrows(InvalidRowException.class, () -> {
            Seat seat = new Seat(1, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setRow(40);
        });
        assertEquals("Seat row exceeds hall max rows.", exception.getMessage());
    }

    @Test
    public void setHall_WithNullHallParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Seat seat = new Seat(1, 1,
                    new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
            seat.setHall(null);
        });
        assertEquals("Hall can not be null", exception.getMessage());
    }

    @Test
    public void getPrice_WithValidSeatObject_ShouldReturnLowPrice() {
        Seat seat = new Seat(1, 1,
                new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        assertEquals(14, seat.getPrice(), 0.001);
    }

    @Test
    public void getPrice_WithValidSeatObject_ShouldReturnHighPrice() {
        Seat seat = new Seat(47, 4,
                new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        assertEquals(26, seat.getPrice(), 0.001);
    }

    @Test
    public void getPrice_WithValidSeatObject_ShouldReturnMiddlePrice() {
        Seat seat = new Seat(21, 2,
                new Hall(2, HallTypeEnum.THREE_D, 4, 12, new ArrayList<>()));
        assertEquals(20, seat.getPrice(), 0.001);
    }
}
