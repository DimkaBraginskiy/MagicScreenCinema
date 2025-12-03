package com.magicscreencinema.domain.associations;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.exception.RecursionException;
import com.magicscreencinema.domain.model.Staff;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class StaffTest {
    @Test
    public void createManagerAssociation_withValidParameter_ShouldCreateReverseAssociation() {
        Staff manager = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);
        Staff staff = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, manager);

        Staff m = staff.getManager().get();
        assertEquals("Fname", m.getFirstName());
        assertEquals("Lname", m.getLastName());
        assertEquals("example@gmail.com", m.getEmail());
        assertEquals("pass", m.getPassword());
        assertEquals(LocalDate.of(1999, 10, 7), m.getBirthDate());
        assertEquals(LocalDate.of(2000, 10, 7), m.getHireDate());
        assertEquals(12.3, m.getSalary(), 0.0001);
        assertEquals(ContractTypeEnum.FULL_TIME, m.getContractType());
        assertTrue(m.getManagedStaff().contains(staff));
        assertEquals(1, manager.getManagedStaff().size());
    }

    @Test
    public void createManagerAssociation_WithItself_ShouldThrowNullAttributeException() {
        Staff manager = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        RecursionException exception = assertThrows(RecursionException.class, () -> {
            manager.assignManager(manager);
        });
        assertEquals("An object cannot have a recursive association with itself", exception.getMessage());
    }

    @Test
    public void createManagerAssociation_WithNullManagerParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                    "pass", LocalDate.of(1999, 10, 7),
                    LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Manager can not be null", exception.getMessage());
    }

    @Test
    public void updateManagerAssociation_WithNullManagerParameter_ShouldThrowNullAttributeException() {
        Staff staff = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            staff.assignManager(null);
        });
        assertEquals("Manager can not be null", exception.getMessage());
    }

    @Test
    public void updateManagerAssociation_withValidParameter_ShouldUpdateReverseAssociation() {
        Staff manager = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        Staff manager2 = new Staff("Fname2", "Lname2", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME);

        Staff staff = new Staff("Fname", "Lname", "2345678", "example@gmail.com",
                "pass", LocalDate.of(1999, 10, 7),
                LocalDate.of(2000, 10, 7), 12.3, ContractTypeEnum.FULL_TIME, manager);

        staff.assignManager(manager2);

        Staff m = staff.getManager().get();
        assertEquals("Fname2", m.getFirstName());
        assertEquals("Lname2", m.getLastName());
        assertFalse(manager.getManagedStaff().contains(staff));
        assertTrue(manager2.getManagedStaff().contains(staff));
        assertEquals(0, manager.getManagedStaff().size());
        assertEquals(1, manager2.getManagedStaff().size());
    }
}
