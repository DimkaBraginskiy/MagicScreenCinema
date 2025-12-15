package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.exception.RecursionException;
import com.magicscreencinema.domain.model.Person;
import com.magicscreencinema.domain.model.Staff;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class StaffTest {
    @Test
    public void createManagerAssociation_withValidParameter_ShouldCreateReverseAssociation() {
        Person manager = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);
        Person staff = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, manager.getStaff().get());

        Staff m = staff.getStaff().get().getManager().get();
        assertEquals("Fname", m.getPerson().getFirstName());
        assertEquals("Lname", m.getPerson().getLastName());
        assertEquals("example@gmail.com", m.getPerson().getEmail());
        assertEquals("pass", m.getPerson().getPassword());
        assertEquals(LocalDate.of(1999, 10, 7), m.getPerson().getBirthDate());
        assertEquals(LocalDate.of(2000, 10, 7), m.getHireDate());
        assertEquals(12.3, m.getSalary(), 0.0001);
        assertEquals(ContractTypeEnum.FULL_TIME, m.getContractType());
        assertTrue(m.getManagedStaff().contains(staff.getStaff().get()));
        assertEquals(1, manager.getStaff().get().getManagedStaff().size());
    }

    @Test
    public void createManagerAssociation_WithItself_ShouldThrowNullAttributeException() {
        Person person = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);
        Staff manager = person.getStaff().get();
        RecursionException exception = assertThrows(RecursionException.class, () -> {
            manager.assignManager(manager);
        });
        assertEquals("An object cannot have a recursive association with itself", exception.getMessage());
    }

    @Test
    public void createManagerAssociation_WithNullManagerParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Person("Fname", "Lname", "2345678", "example@gmail.com",
                    "pass", LocalDate.of(1999, 10, 7),
                    LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Manager can not be null", exception.getMessage());
    }

    @Test
    public void updateManagerAssociation_WithNullManagerParameter_ShouldThrowNullAttributeException() {
        Person person = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);
        Staff staff = person.getStaff().get();

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            staff.assignManager(null);
        });
        assertEquals("Manager can not be null", exception.getMessage());
    }

    @Test
    public void updateManagerAssociation_withValidParameter_ShouldUpdateReverseAssociation() {
        Person manager = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        Person manager2 = new Person("Fname2", "Lname2", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        Person staff = new Person("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, manager.getStaff().get());

        staff.getStaff().get().assignManager(manager2.getStaff().get());

        Staff m = staff.getStaff().get().getManager().get();
        assertEquals("Fname2", m.getPerson().getFirstName());
        assertEquals("Lname2", m.getPerson().getLastName());
        assertFalse(manager.getStaff().get().getManagedStaff().contains(staff.getStaff().get()));
        assertTrue(manager2.getStaff().get().getManagedStaff().contains(staff.getStaff().get()));
        assertEquals(0, manager.getStaff().get().getManagedStaff().size());
        assertEquals(1, manager2.getStaff().get().getManagedStaff().size());
    }
}
