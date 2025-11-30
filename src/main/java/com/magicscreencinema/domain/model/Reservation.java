package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.PaymentMethodEnum;
import com.magicscreencinema.domain.enums.PaymentStatusEnum;
import com.magicscreencinema.domain.enums.ReservationStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import java.time.LocalDateTime;
import java.util.*;

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
    private Set<Payment> payments;
    private Reservation() {}
    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status) {
        this.reservationNumber = UUID.randomUUID();
        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
        this.payments = new HashSet<>();
    }
    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status, Discount discount, List<Seat> seats, Seance seance) {
        this(reservationTime, status);

        // this.discount = FieldValidator.validateObjectNotNull(discount, "discount"); // TODO dima eto twoe
        // this.seats = FieldValidator.validateSeatList(seats, "Seats"); // TODO dima eto twoe

        setSeance(seance);
    }

    void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Payment addPayment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId) {
        Payment newPayment = new Payment(paymentMethod, paymentStatus, transactionId, this);
        this.payments.add(newPayment);
        return newPayment;
    }

    public Optional<Payment> getCompletedPayment() {
        return payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatusEnum.COMPLETED)
                .findFirst();
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
    public Seance getSeance() {
        return this.seance;
    }
    public Set<Payment> getPayments() {
        return Collections.unmodifiableSet(payments);
    }
}
