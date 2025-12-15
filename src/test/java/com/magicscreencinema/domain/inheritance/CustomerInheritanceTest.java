package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.InheritanceViolationException;
import com.magicscreencinema.domain.exception.NegativeValueException;
import com.magicscreencinema.domain.model.Customer;
import com.magicscreencinema.domain.model.Person;
import com.magicscreencinema.domain.model.Staff;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CustomerInheritanceTest {
    @Test
    public void constructor_WithLoyaltyPointsGiven_ShouldCreateCustomerOnly(){
        Person person = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                150
        );

        assertTrue(person.getCustomer().isPresent());
        assertFalse(person.getStaff().isPresent());

        Customer customer = person.getCustomer().get();

        assertEquals(150, customer.getLoyaltyPoints());
        assertEquals(person, customer.getPerson());
    }

    @Test
    public void constructor_WhenPersonAlreadyHasCustomer_ShouldThrowException(){
        Person person = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                150
        );

        assertThrows(InheritanceViolationException.class, () -> {
            new Customer(
                    person,
                    150
            );
        });
    }
}
