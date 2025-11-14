package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ReservationStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ElementCollection(name = "reservations")
public class Reservation {
    @Id
    private UUID reservationNumber;
    private LocalDateTime reservationTime;
    private ReservationStatusEnum status;

    @ManyToOne()
    private Discount discount;
    @OneToMany(cascade = {Cascade.DELETE, Cascade.SAVE}, fetch = Fetch.EAGER)
    private List<Seat> seats;

    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status, Discount discount, List<Seat> seats) {
        this.reservationNumber = UUID.randomUUID();

        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
        this.status = FieldValidator.validateObjectNotNull(status, "Status");

        this.discount = discount;
        this.seats = FieldValidator.validateSeatList(seats, "Seats");
    }

    private Reservation() {
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = FieldValidator.validateSeatList(seats, "Seats");
    }

    public UUID getReservationNumber() {
        return reservationNumber;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public Optional<Discount> getDiscount() {
        return Optional.ofNullable(discount);
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public double getTotalPrice() {
        List<Seat> seatList = Objects.requireNonNullElse(seats, List.of());

        double total = 0.0;
        for (Seat seat : seatList) {
            if (seat != null) {
                total += seat.getPrice();
            }
        }
        double discountAmount = discount == null ? 0.0 : total * discount.getDiscountAmount();
        total -= discountAmount;

        return Math.max(0.0, total);
    }
}
