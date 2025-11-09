package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ReservationStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Reservation {
    private UUID reservationNumber;
    private LocalDateTime reservationTime;
    private ReservationStatusEnum status;

    private Discount discount;
    private List<Seat> seats;

    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status) {
        this.reservationNumber = UUID.randomUUID();

        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
    }

    public void setDiscount(Discount discount) {
        this.discount = FieldValidator.validateObjectNotNull(discount, "Discount");
    }

    public void setSeats(List<Seat> seats) {
        this.seats = FieldValidator.validateSeatList(seats, "Seats");
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public Discount getDiscount() {
        return discount;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
