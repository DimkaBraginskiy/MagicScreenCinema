package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.InheritanceViolationException;
import com.magicscreencinema.domain.model.Customer;
import com.magicscreencinema.domain.model.Person;
import com.magicscreencinema.domain.model.Staff;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class StaffInheritanceTest {
    @Test
    public void constructor_WithStaffParametersGiven_ShouldCreateStaffOnly(){
        Person person = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                LocalDate.of(2015, 10, 3),
                8000,
                ContractTypeEnum.FULL_TIME
        );

        assertTrue(person.getStaff().isPresent());
        assertFalse(person.getCustomer().isPresent());

        Staff staff = person.getStaff().get();

        assertEquals(LocalDate.of(2015,10,3), staff.getHireDate());
        assertEquals(8000, staff.getSalary(), 0.0);
        assertEquals(ContractTypeEnum.FULL_TIME, staff.getContractType());
        assertEquals(person, staff.getPerson());
    }

    @Test
    public void constructor_WhenPersonAlreadyHasStaff_ShouldThrowException(){
        Person person = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                LocalDate.of(2015, 10, 3),
                8000,
                ContractTypeEnum.FULL_TIME
        );

        assertThrows(InheritanceViolationException.class, () -> {
            new Staff(
                    person,
                    LocalDate.of(2022,1,1),
                    3000,
                    ContractTypeEnum.FULL_TIME
            );
        });
    }
}
