package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.PaymentMethodEnum;
import com.magicscreencinema.domain.enums.PaymentStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.UUID;

@ElementCollection(name = "payments")
public class Payment {
    //--basic fields
    @Id
    private UUID id;
    private PaymentMethodEnum paymentMethod;
    private PaymentStatusEnum paymentStatus;
    private String transactionId;

    //--associations
    private Reservation reservation;

    //--constructors
    private Payment() {}
    public Payment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId) {
        this.paymentMethod = FieldValidator.validateObjectNotNull(paymentMethod, "Payment Method");
        this.paymentStatus = FieldValidator.validateObjectNotNull(paymentStatus, "Payment Status");
        this.transactionId = FieldValidator.validateNullOrEmptyString(transactionId, "Transaction ID");
        id = UUID.randomUUID();
    }

    public Payment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId, Reservation reservation){
        this(paymentMethod, paymentStatus, transactionId);
        setReservation(reservation);
    }

    //--association logic
    public void setReservation(Reservation newReservation) {
        FieldValidator.validateObjectNotNull(newReservation, "Reservation");

        if (this.reservation == newReservation) return;

        // break link with old reservation
        if (this.reservation != null) {
            Reservation oldReservation = this.reservation;
            this.reservation = null;
            oldReservation.deletePayment();
        }

        this.reservation = newReservation;

        if (newReservation.getPayment() != this) {
            newReservation.setPayment(this);
        }
    }
    protected void deleteReservation() {
        this.reservation = null;
    }

    //--getters
    public UUID getId() {
        return id;
    }
    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }
    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public Reservation getReservation() {
        return reservation;
    }

    //--setters
    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = FieldValidator.validateObjectNotNull(paymentMethod, "Payment Method");
    }
    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = FieldValidator.validateObjectNotNull(paymentStatus, "Payment Status");
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = FieldValidator.validateNullOrEmptyString(transactionId, "Transaction ID");
    }
}