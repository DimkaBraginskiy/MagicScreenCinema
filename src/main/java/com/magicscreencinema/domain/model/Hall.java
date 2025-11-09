package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.util.List;

public class Hall {
    private int hallNumber;
    private HallTypeEnum hallType;
    private int maxRow;
    private int rowWidth;

    private List<Seat> seats;

    public Hall(int hallNumber, HallTypeEnum hallType, int maxRow, int rowWidth, List<Seat> seats) {
        this.hallNumber = FieldValidator.validatePositiveNumber(hallNumber, "Hall Number");
        this.hallType = FieldValidator.validateObjectNotNull(hallType, "Hall Type");
        this.maxRow = FieldValidator.validatePositiveNumber(maxRow, "Max Row");
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");
        this.seats = FieldValidator.validateSeatsInHallNotNull(seats, this);
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public void setRowWidth(int rowWidth) {
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");
        ;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = FieldValidator.validateSeatsInHallNotNull(seats, this);
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = FieldValidator.validatePositiveNumber(hallNumber, "Hall Number");
    }

    public HallTypeEnum getHallType() {
        return hallType;
    }

    public void setHallType(HallTypeEnum hallType) {
        this.hallType = FieldValidator.validateObjectNotNull(hallType, "Hall Type");
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = FieldValidator.validatePositiveNumber(maxRow, "Max Row");
    }
}
