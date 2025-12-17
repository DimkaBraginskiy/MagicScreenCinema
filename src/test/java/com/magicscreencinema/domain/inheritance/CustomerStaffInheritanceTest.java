package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.model.Customer;
import com.magicscreencinema.domain.model.Person;
import com.magicscreencinema.domain.model.Staff;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.*;

public class CustomerStaffInheritanceTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateBothInstancesAndLinkToPerson() {
        Person person = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                LocalDate.of(2015, 10, 3),
                8000,
                ContractTypeEnum.FULL_TIME,
                150
        );

        assertTrue(person.getStaff().isPresent());
        assertTrue(person.getCustomer().isPresent());

        Staff staff = person.getStaff().get();
        Customer customer = person.getCustomer().get();

        //Staff
        assertEquals(LocalDate.of(2015,10,3), staff.getHireDate());
        assertEquals(8000, staff.getSalary(), 0.0);
        assertEquals(ContractTypeEnum.FULL_TIME, staff.getContractType());
        assertEquals(person, staff.getPerson());

        //Customer
        assertEquals(150, customer.getLoyaltyPoints());
        assertEquals(person, customer.getPerson());
    }

    @Test
    public void constructor_WithValidParametersAndManager_ShouldCreateStaffCustomerAndAssignManager(){
        Person managerPerson = new Person(
                "Saul",
                "Goodman",
                "756-745-33",
                "manager@gmail.com",
                "pass",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2022, 1, 1),
                6700,
                ContractTypeEnum.FULL_TIME
        );

        Staff manager = managerPerson.getStaff().get();

        Person employee = new Person(
                "Test first",
                "Test last",
                "756-745-33",
                "example@gmail.com",
                "pass",
                LocalDate.of(1990, 10, 3),
                LocalDate.of(2015, 10, 3),
                8000,
                ContractTypeEnum.FULL_TIME,
                manager,
                150
        );

        assertTrue(employee.getStaff().isPresent());
        assertTrue(employee.getCustomer().isPresent());

        Staff staff = employee.getStaff().get();

        //manager
        assertTrue(staff.getManager().isPresent());
        assertSame(manager, staff.getManager().get());

        //bidirectional check
        assertTrue(manager.getManagedStaff().contains(staff));

        //ownership
        assertSame(employee, staff.getPerson());
        assertEquals(employee, employee.getCustomer().get().getPerson());
    }
}
