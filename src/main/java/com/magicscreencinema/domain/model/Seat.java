package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.InvalidRowException;
import com.magicscreencinema.domain.validation.FieldValidator;

public class Seat {
    private int seatNumber;
    private int row;
    private static final double PRICE = 20;

    private Hall hall;

    public Seat(int seatNumber, int row, Hall hall) {
        this.seatNumber = FieldValidator.validatePositiveNumber(seatNumber, "Seat Number");
        this.hall = FieldValidator.validateObjectNotNull(hall, "Hall");

        if (row > hall.getMaxRow()) throw new InvalidRowException("Seat row exceeds hall max rows.");
        this.row = FieldValidator.validatePositiveNumber(row, "Row");
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = FieldValidator.validatePositiveNumber(seatNumber, "Seat Number");
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row > hall.getMaxRow()) throw new InvalidRowException("Seat row exceeds hall max rows.");
        this.row = FieldValidator.validatePositiveNumber(row, "Row");
    }

    public double getPrice() {
        int row = this.row;
        int maxRow = hall.getMaxRow();

        double segment = maxRow / 3.0;

        if (row <= segment) {
            return PRICE * 0.7;     // front - cheaper
        } else if (row <= segment * 2) {
            return PRICE;           // default
        } else {
            return PRICE * 1.3;     // back - expensive
        }
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = FieldValidator.validateObjectNotNull(hall, "Hall");
    }
}
