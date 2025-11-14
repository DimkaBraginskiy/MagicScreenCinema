package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.PaymentMethodEnum;
import com.magicscreencinema.domain.enums.PaymentStatusEnum;
import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreatePayment() {
        Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");

        assertNotNull(payment);
        assertEquals(PaymentMethodEnum.CARD, payment.getPaymentMethod());
        assertEquals(PaymentStatusEnum.PENDING, payment.getPaymentStatus());
        assertEquals("fff", payment.getTransactionId());
    }

    @Test
    public void constructor_WithNullPaymentMethodField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Payment(null, PaymentStatusEnum.PENDING, "fff");
        });
        assertEquals("Payment Method can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullPaymentStatusField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Payment(PaymentMethodEnum.CARD, null, "fff");
        });
        assertEquals("Payment Status can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullTransactionIdField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, null);
        });
        assertEquals("Transaction ID can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyTransactionIdField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "");
        });
        assertEquals("Transaction ID can not be empty", exception.getMessage());
    }

    @Test
    public void setPaymentMethod_WithNullPaymentMethodParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
            payment.setPaymentMethod(null);
        });
        assertEquals("Payment Method can not be null", exception.getMessage());
    }

    @Test
    public void setPaymentMethod_WithValidPaymentMethodParameter_ShouldChangePaymentMethod() {
        Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
        payment.setPaymentMethod(PaymentMethodEnum.CASH);

        assertEquals(PaymentMethodEnum.CASH, payment.getPaymentMethod());
    }

    @Test
    public void setPaymentStatus_WithNullPaymentStatusParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
            payment.setPaymentStatus(null);
        });
        assertEquals("Payment Status can not be null", exception.getMessage());
    }

    @Test
    public void setPaymentStatus_WithValidPaymentStatusParameter_ShouldChangePaymentStatus() {
        Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
        payment.setPaymentStatus(PaymentStatusEnum.COMPLETED);

        assertEquals(PaymentStatusEnum.COMPLETED, payment.getPaymentStatus());
    }

    @Test
    public void setTransactionId_WithNullTransactionIdParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
            payment.setTransactionId(null);
        });
        assertEquals("Transaction ID can not be null", exception.getMessage());
    }

    @Test
    public void setTransactionId_WithEmptyTransactionIdParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
            payment.setTransactionId("");
        });
        assertEquals("Transaction ID can not be empty", exception.getMessage());
    }

    @Test
    public void setPaymentStatus_WithValidTransactionIdParameter_ShouldChangeTransactionId() {
        Payment payment = new Payment(PaymentMethodEnum.CARD, PaymentStatusEnum.PENDING, "fff");
        payment.setTransactionId("nnn");

        assertEquals("nnn", payment.getTransactionId());
    }
}
