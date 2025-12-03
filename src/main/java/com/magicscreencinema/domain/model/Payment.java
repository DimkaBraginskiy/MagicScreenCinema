package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.PaymentMethodEnum;
import com.magicscreencinema.domain.enums.PaymentStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;
import com.magicscreencinema.persistence.declaration.ManyToOne;

import java.util.UUID;

@ElementCollection(name = "payments")
public class Payment {
    @Id
    private UUID id;
    private PaymentMethodEnum paymentMethod;
    private PaymentStatusEnum paymentStatus;
    private String transactionId;
    @ManyToOne
    private Reservation reservation;

    private Payment() {}
    Payment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId, Reservation reservation) {
        this.paymentMethod = FieldValidator.validateObjectNotNull(paymentMethod, "Payment Method");
        this.paymentStatus = FieldValidator.validateObjectNotNull(paymentStatus, "Payment Status");
        this.transactionId = FieldValidator.validateNullOrEmptyString(transactionId, "Transaction ID");
        this.reservation = FieldValidator.validateObjectNotNull(reservation, "Reservation");
        id = UUID.randomUUID();
    }

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