package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void constructor_WithValidParametersPhoneNumberNotNullField_ShouldCreateCustomer() {
        Customer customer = new Customer("Test first", "Test last", "756-745-33",
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);

        assertEquals("Test first", customer.getFirstName());
        assertEquals("Test last", customer.getLastName());
        assertEquals("756-745-33", customer.getPhoneNumber().get());
        assertEquals("example@gmail.com", customer.getEmail());
        assertEquals("pass", customer.getPassword());
        assertEquals(LocalDate.of(1990, 10, 3), customer.getBirthDate());
        assertEquals(0, customer.getLoyaltyPoints());
    }

    @Test
    public void constructor_WithValidParametersPhoneNumberNullField_ShouldCreateCustomer() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);

        assertEquals("Test first", customer.getFirstName());
        assertEquals("Test last", customer.getLastName());
        assertTrue(customer.getPhoneNumber().isEmpty());
        assertEquals("example@gmail.com", customer.getEmail());
        assertEquals("pass", customer.getPassword());
        assertEquals(LocalDate.of(1990, 10, 3), customer.getBirthDate());
        assertEquals(0, customer.getLoyaltyPoints());
    }

    @Test
    public void constructor_WithNullFirstNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Customer(null, "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("First Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyFirstNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Customer("", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("First Name can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullLastNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Customer("Test first", null, null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("Last Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyLastNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Customer("Test first", "", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("Last Name can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullPasswordField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "example@gmail.com", null,
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("Password can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyPasswordField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "example@gmail.com", "",
                    LocalDate.of(1990, 10, 3), 0);
        });
        assertEquals("Password can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullBirthDateField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass", null, 0);
        });
        assertEquals("Birth Date can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithFutureBirthDateField_ShouldThrowDateNotInTheFutureException() {
        DateInFutureException exception = assertThrows(DateInFutureException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(2026, 10, 3), 0);
        });
        assertEquals("Birth Date can not be in the future", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeLoyaltyPointsField_ShouldThrowNegativeValueException() {
        NegativeValueException exception = assertThrows(NegativeValueException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), -10);
        });
        assertEquals("Loyalty Points must be a non-negative value ( >= 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNullEmailField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Customer("Test first", "Test last", null,
                    null, "pass",
                    LocalDate.of(1990, 10, 3), 10);
        });
        assertEquals("Email can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyEmailField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "", "pass",
                    LocalDate.of(1990, 10, 3), 10);
        });
        assertEquals("Email can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidEmailFormatField_ShouldThrowInvalidEmailFormatException() {
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            new Customer("Test first", "Test last", null,
                    "ffff", "pass",
                    LocalDate.of(1990, 10, 3), 10);
        });
        assertEquals("Email format is invalid", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyPhoneNumberField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Customer("Test first", "Test last", "",
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 10);
        });
        assertEquals("Phone Number can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidPhoneNumberFormatField_ShouldThrowInvalidPhoneNumberFormatException() {
        InvalidPhoneNumberFormatException exception = assertThrows(InvalidPhoneNumberFormatException.class, () -> {
            new Customer("Test first", "Test last", "fff",
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 10);
        });
        assertEquals("Phone Number format is invalid", exception.getMessage());
    }

    @Test
    public void setFirstName_WithNullFirstNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setFirstName(null);
        });
        assertEquals("First Name can not be null", exception.getMessage());
    }

    @Test
    public void setFirstName_WithEmptyFirstNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setFirstName("");
        });
        assertEquals("First Name can not be empty", exception.getMessage());
    }

    @Test
    public void setFirstName_WithValidFirstNameParameter_ShouldChangeFirstName() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setFirstName("New First");

        assertEquals("New First", customer.getFirstName());
    }

    @Test
    public void setLastName_WithNullLastNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setLastName(null);
        });
        assertEquals("Last Name can not be null", exception.getMessage());
    }

    @Test
    public void setLastName_WithEmptyLastNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setLastName("");
        });
        assertEquals("Last Name can not be empty", exception.getMessage());
    }

    @Test
    public void setFirstName_WithValidLastNameParameter_ShouldChangeLastName() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setLastName("New Last");

        assertEquals("New Last", customer.getLastName());
    }

    @Test
    public void setPassword_WithNullPasswordParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setPassword(null);
        });
        assertEquals("Password can not be null", exception.getMessage());
    }

    @Test
    public void setPassword_WithEmptyPasswordParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setPassword("");
        });
        assertEquals("Password can not be empty", exception.getMessage());
    }

    @Test
    public void setPassword_WithValidPasswordParameter_ShouldChangePassword() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setPassword("new password");

        assertEquals("new password", customer.getPassword());
    }

    @Test
    public void setBirthDate_WithNullBirthDateParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setBirthDate(null);
        });
        assertEquals("Birth Date can not be null", exception.getMessage());
    }

    @Test
    public void setBirthDate_WithFutureBirthDateParameter_ShouldThrowDateNotInTheFutureException() {
        DateInFutureException exception = assertThrows(DateInFutureException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setBirthDate(LocalDate.of(2026, 10, 3));
        });
        assertEquals("Birth Date can not be in the future", exception.getMessage());
    }

    @Test
    public void setBirthDate_WithValidBirthDateParameter_ShouldChangeBirthDate() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setBirthDate(LocalDate.of(2023, 10, 3));

        assertEquals(LocalDate.of(2023, 10, 3), customer.getBirthDate());
    }

    @Test
    public void setLoyaltyPoints_WithNegativeLoyaltyPointsParameter_ShouldThrowNegativeValueException() {
        NegativeValueException exception = assertThrows(NegativeValueException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setLoyaltyPoints(-20);
        });
        assertEquals("Loyalty Points must be a non-negative value ( >= 0).", exception.getMessage());
    }

    @Test
    public void setLoyaltyPoints_WithPositiveLoyaltyPointsParameter_ShouldChangeLoyaltyPoints() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setLoyaltyPoints(30);

        assertEquals(30, customer.getLoyaltyPoints());
    }

    @Test
    public void setLoyaltyPoints_WithZeroLoyaltyPointsParameter_ShouldChangeLoyaltyPoints() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setLoyaltyPoints(0);

        assertEquals(0, customer.getLoyaltyPoints());
    }

    @Test
    public void setEmail_WithNullEmailParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setEmail(null);
        });
        assertEquals("Email can not be null", exception.getMessage());
    }

    @Test
    public void setEmail_WithEmptyEmailParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setEmail("");
        });
        assertEquals("Email can not be empty", exception.getMessage());
    }

    @Test
    public void setEmail_WithInvalidEmailFormatParameter_ShouldThrowInvalidEmailFormatException() {
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", null,
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setEmail("fff");
        });
        assertEquals("Email format is invalid", exception.getMessage());
    }

    @Test
    public void setEmail_WithValidEmailParameter_ShouldChangeEmail() {
        Customer customer = new Customer("Test first", "Test last", null,
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 0);
        customer.setEmail("john@gmail.com");

        assertEquals("john@gmail.com", customer.getEmail());
    }

    @Test
    public void setPhoneNumber_WithEmptyPhoneNumberParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 0);
            customer.setPhoneNumber("");
        });
        assertEquals("Phone Number can not be empty", exception.getMessage());
    }

    @Test
    public void setPhoneNumber_WithInvalidPhoneNumberFormatParameter_ShouldThrowInvalidPhoneNumberFormatException() {
        InvalidPhoneNumberFormatException exception = assertThrows(InvalidPhoneNumberFormatException.class, () -> {
            Customer customer = new Customer("Test first", "Test last", "fff",
                    "example@gmail.com", "pass",
                    LocalDate.of(1990, 10, 3), 10);
            customer.setPhoneNumber("fff");
        });
        assertEquals("Phone Number format is invalid", exception.getMessage());
    }

    @Test
    public void setPhoneNumber_WithValidPhoneNumberFormatParameter_ShouldChangePhoneNumber() {
        Customer customer = new Customer("Test first", "Test last", "9876543",
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 10);
        customer.setPhoneNumber("1234567");

        assertEquals("1234567", customer.getPhoneNumber().get());
    }

    @Test
    public void setPhoneNumber_WithNullPhoneNumberParameter_ShouldChangePhoneNumber() {
        Customer customer = new Customer("Test first", "Test last", "9876543",
                "example@gmail.com", "pass",
                LocalDate.of(1990, 10, 3), 10);
        customer.setPhoneNumber(null);

        assertTrue(customer.getPhoneNumber().isEmpty());
    }
}
