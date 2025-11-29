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
    //--basic fields
    @Id
    private UUID reservationNumber;
    private LocalDateTime reservationTime;
    private ReservationStatusEnum status;

    //--associations
    @ManyToOne()
    private Discount discount;
    @OneToMany(cascade = {Cascade.DELETE, Cascade.SAVE}, fetch = Fetch.EAGER)
    private List<Seat> seats;

    private Seance seance;
    private Payment payment;

    //--constructors
    private Reservation() {}
    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status) {
        this.reservationNumber = UUID.randomUUID();
        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
    }
    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status, Discount discount, List<Seat> seats, Seance seance, Payment payment) {
        this(reservationTime, status);

        // this.discount = FieldValidator.validateObjectNotNull(discount, "discount"); // TODO dima eto twoe
        // this.seats = FieldValidator.validateSeatList(seats, "Seats"); // TODO dima eto twoe

        setSeance(seance);
        assignPayment(payment);
    }

    //--association logic
    //seance
    void setSeance(Seance seance) {
        this.seance = seance;
    }

    //payment
    public void assignPayment(Payment newPayment) {
        // if we are just clearing the payment (setting null)
        if (newPayment == null) {
            if (this.payment != null) {
                this.payment.assignReservation(null); // clear the reverse side (setting to null)
                this.payment = null;
            }
            return;
        }

        FieldValidator.validateObjectNotNull(newPayment, "Payment");
        if (this.payment == newPayment) return;

        // if the new payment is already assigned to ANOTHER reservation
        if (newPayment.getReservation() != null && newPayment.getReservation() != this) {
            newPayment.getReservation().assignPayment(null);
        }

        // unlink ourselves from our currently assigned payment (if any)
        if (this.payment != null) {
            this.payment.assignReservation(null);
        }

        this.payment = newPayment;
        this.payment.assignReservation(this);
    }

    //--setters
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

    //--getters
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
    public Seance getSeance() {
        return this.seance;
    }
    public Payment getPayment() {
        return this.payment;
    }
}
