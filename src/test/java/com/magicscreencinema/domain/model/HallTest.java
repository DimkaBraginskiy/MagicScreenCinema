package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.exception.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HallTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateHall() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
        Seat seat = new Seat(2, 4, hall);
        hall.setSeats(List.of(seat));

        assertEquals(1, hall.getSeats().size());
        assertEquals(HallTypeEnum.IMAX, hall.getHallType());
        assertEquals(2, hall.getHallNumber());
        assertEquals(10, hall.getMaxRow());
        assertEquals(12, hall.getRowWidth());
    }

    @Test
    public void constructor_WithNegativeHallNumberField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(-10, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Hall Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroHallNumberField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(0, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Hall Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNullHallTypeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Hall hall = new Hall(2, null, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Hall Type can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeMaxRowField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, -3, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Max Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroMaxRowField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 0, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Max Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeRowWidthField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, -2, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Row Width must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroRawWidthField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 0, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Row Width must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNullSeatsField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Hall(2, HallTypeEnum.IMAX, 10, 12, null);
        });
        assertEquals("Seats list can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidRowFieldForSeatsField_ShouldThrowInvalidRowException() {
        InvalidRowException exception = assertThrows(InvalidRowException.class, () -> {
            Seat seat = new Seat(2, 40, new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>()));
            new Hall(2, HallTypeEnum.IMAX, 10, 12, List.of(seat));
        });
        assertEquals("Seat row exceeds hall max rows.", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidSeatNumberFieldForSeatsField_ShouldThrowInvalidSeatNumberException() {
        InvalidSeatNumberException exception = assertThrows(InvalidSeatNumberException.class, () -> {
            Seat seat = new Seat(40, 1, new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>()));
            new Hall(2, HallTypeEnum.IMAX, 10, 12, List.of(seat));
        });
        assertEquals("Seat number exceeds hall row width.", exception.getMessage());
    }

    @Test
    public void constructor_WithNullSeatObjectForSeatsField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            ArrayList<Seat> seats = new ArrayList<>();
            seats.add(null);

            Seat seat = new Seat(40, 1, new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>()));
            new Hall(2, HallTypeEnum.IMAX, 10, 12, seats);
        });
        assertEquals("Seat can not be null", exception.getMessage());
    }

    @Test
    public void setHallNumber_WithNegativeHallNumberParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(10, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
            hall.setHallNumber(-2);
        });
        assertEquals("Hall Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setHallNumber_WithZeroHallNumberParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
            hall.setHallNumber(0);
        });
        assertEquals("Hall Number must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setHallNumber_WithValidHallNumberParameter_ShouldChangeHallNumber() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
        Seat seat = new Seat(2, 4, hall);
        hall.setSeats(List.of(seat));
        hall.setHallNumber(20);

        assertEquals(20, hall.getHallNumber());
    }

    @Test
    public void setHallType_WithNullHallTypeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
            hall.setHallType(null);
        });
        assertEquals("Hall Type can not be null", exception.getMessage());
    }

    @Test
    public void setHallType_WithValidHallTypeParameter_ShouldChangeHallType() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
        Seat seat = new Seat(2, 4, hall);
        hall.setSeats(List.of(seat));
        hall.setHallType(HallTypeEnum.THREE_D);

        assertEquals(HallTypeEnum.THREE_D, hall.getHallType());
    }

    @Test
    public void setMaxRow_WithNegativeMaxRowParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 3, 12, new ArrayList<>());
            Seat seat = new Seat(1, 1, hall);
            hall.setSeats(List.of(seat));
            hall.setMaxRow(-2);
        });
        assertEquals("Max Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setMaxRow_WithZeroMaxRowParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 3, 12, new ArrayList<>());
            Seat seat = new Seat(1, 1, hall);
            hall.setSeats(List.of(seat));
            hall.setMaxRow(0);
        });
        assertEquals("Max Row must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setMaxRow_WithValidMaxRowParameter_ShouldChangeMaxRow() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 3, 12, new ArrayList<>());
        Seat seat = new Seat(1, 1, hall);
        hall.setSeats(List.of(seat));
        hall.setMaxRow(30);

        assertEquals(30, hall.getMaxRow());
    }

    @Test
    public void setRowWidth_WithNegativeRowWidthParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 2, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
            hall.setRowWidth(-2);
        });
        assertEquals("Row Width must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setRowWidth_WithZeroRawWidthParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 0, new ArrayList<>());
            Seat seat = new Seat(2, 4, hall);
            hall.setSeats(List.of(seat));
            hall.setRowWidth(0);
        });
        assertEquals("Row Width must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setRowWidth_WithValidRowWidthParameter_ShouldChangeRowWidth() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 20, new ArrayList<>());
        Seat seat = new Seat(2, 4, hall);
        hall.setSeats(List.of(seat));
        hall.setRowWidth(10);

        assertEquals(10, hall.getRowWidth());
    }

    @Test
    public void setSeats_WithNullSeatsParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 10, new ArrayList<>());
            hall.setSeats(null);
        });
        assertEquals("Seats list can not be null", exception.getMessage());
    }

    @Test
    public void setSeats_WithInvalidRowFieldForSeatsParameter_ShouldThrowInvalidRowException() {
        InvalidRowException exception = assertThrows(InvalidRowException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(2, 40, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Seat row exceeds hall max rows.", exception.getMessage());
    }

    @Test
    public void setSeats_WithNullSeatObjectForSeatsParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            ArrayList<Seat> seats = new ArrayList<>();
            seats.add(null);

            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            hall.setSeats(seats);
        });
        assertEquals("Seat can not be null", exception.getMessage());
    }

    @Test
    public void setSeats_WithInvalidSeatNumberFieldForSeatsParameter_ShouldThrowInvalidSeatNumberException() {
        InvalidSeatNumberException exception = assertThrows(InvalidSeatNumberException.class, () -> {
            Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
            Seat seat = new Seat(40, 2, hall);
            hall.setSeats(List.of(seat));
        });
        assertEquals("Seat number exceeds hall row width.", exception.getMessage());
    }

    @Test
    public void setSeats_WithValidSeatsParameter_ShouldChangeSetsList() {
        Hall hall = new Hall(2, HallTypeEnum.IMAX, 10, 12, new ArrayList<>());
        Seat seat = new Seat(4, 1, hall);
        hall.setSeats(List.of(seat));
    }
}
