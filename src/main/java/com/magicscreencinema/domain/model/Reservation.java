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
    @ManyToOne
    private Discount discount;
    @OneToMany(fetch = Fetch.EAGER)
    private Set<Seat> seats;
    @ManyToOne
    private Seance seance;
    @OneToMany(fetch = Fetch.EAGER, cascade = {Cascade.SAVE, Cascade.DELETE})
    private Set<Payment> payments;
    @ManyToOne
    private Customer customer;

    private Reservation() {
    }

    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status, Seance seance, Set<Seat> seats, Customer customer) {
        this.reservationNumber = UUID.randomUUID();
        this.reservationTime = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
        this.payments = new HashSet<>();
        assignSeance(seance);
        for (Seat seat : seats) {
            addSeat(seat);
        }

        assignCustomer(customer);
    }

    public Reservation(LocalDateTime reservationTime, ReservationStatusEnum status, Seance seance, Set<Seat> seats, Customer customer, Discount discount) {
        this(reservationTime, status, seance, seats, customer);
        assignDiscount(discount);
    }

    public void assignCustomer(Customer customer) {
        FieldValidator.validateObjectNotNull(customer, "Customer");
        if (this.customer != null) {
            this.customer.removeReservation(this);
        }

        this.customer = customer;

        customer.addReservation(this);
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void assignSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        seance.addReservation(this);
        this.seance = seance;
    }

    public void changeSeance(Seance newSeance) {
        FieldValidator.validateObjectNotNull(newSeance, "Seance");
        this.seance.removeReservation(this);
        newSeance.addReservation(this);
        this.seance = newSeance;
    }

    public Payment addPayment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId) {
        Payment newPayment = new Payment(paymentMethod, paymentStatus, transactionId, this);
        this.payments.add(newPayment);
        return newPayment;
    }

    public void assignDiscount(Discount discount) {
        FieldValidator.validateObjectNotNull(discount, "Discount");
        this.discount = discount;
        this.discount.addReservation(this);
    }

    public void removeDiscount() {
        if (this.discount != null) {
            this.discount.removeReservation(this);
            this.discount = null;
        }
    }

    public void addSeat(Seat seat) {
        FieldValidator.validateObjectNotNull(seat, "Seat");
        if (this.seats == null) {
            this.seats = new HashSet<>();
        }
        this.seats.add(seat);
        seat.setReservation(this);
    }

    public void removeSeat(Seat seat) {
        FieldValidator.validateObjectNotNull(seat, "Seat");
        if (this.seats.contains(seat)) {
            this.seats.remove(seat);
            seat.setReservation(null);
        }
    }

    public Optional<Payment> getCompletedPayment() {
        return payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatusEnum.COMPLETED)
                .findFirst();
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        FieldValidator.validateObjectNotNull(customer, "Customer");
        LocalDateTime validated = FieldValidator.validateDateTimeNotInThePast(reservationTime, "Reservation Time");

        ReservationKey oldKey = new ReservationKey(this.reservationNumber, this.reservationTime);
        Reservation oldReservation = customer.getReservations().get(oldKey);
        customer.removeReservation(oldReservation);
        this.reservationTime = validated;
        customer.addReservation(this);
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = FieldValidator.validateObjectNotNull(status, "Status");
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
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

    public Set<Seat> getSeats() {
        return Collections.unmodifiableSet(seats);
    }

    public double getTotalPrice() {
        Set<Seat> seatList = Objects.requireNonNullElse(seats, Set.of());

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
