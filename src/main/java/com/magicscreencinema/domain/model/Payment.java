package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.PaymentMethodEnum;
import com.magicscreencinema.domain.enums.PaymentStatusEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

public class Payment {
    protected PaymentMethodEnum paymentMethod;
    protected PaymentStatusEnum paymentStatus;
    public String transactionId;

    public Payment(PaymentMethodEnum paymentMethod, PaymentStatusEnum paymentStatus, String transactionId) {
        this.paymentMethod = FieldValidator.validateObjectNotNull(paymentMethod, "Payment Method");
        this.paymentStatus = FieldValidator.validateObjectNotNull(paymentStatus, "Payment Status");
        this.transactionId = FieldValidator.validateNullOrEmptyString(transactionId, "Transaction ID");
    }

    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = FieldValidator.validateObjectNotNull(paymentMethod, "Payment Method");
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = FieldValidator.validateObjectNotNull(paymentStatus, "Payment Status");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = FieldValidator.validateNullOrEmptyString(transactionId, "Transaction ID");
    }
}
