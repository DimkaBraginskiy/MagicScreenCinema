package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.DateInFutureException;
import com.magicscreencinema.domain.exception.NonPositiveValueException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.exception.RecursionException;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class StaffTest {
    @Test
    public void constructor_WithValidParametersAndNullManagerField_ShouldCreateStaff() {
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2025, 3, 10), 20_000, ContractTypeEnum.FULL_TIME,
                null);

        assertEquals("Test first", staff.getFirstName());
        assertEquals("Test last", staff.getLastName());
        assertEquals("1234567", staff.getPhoneNumber().get());
        assertEquals("example@gmail.com", staff.getEmail());
        assertEquals("pass", staff.getPassword());
        assertEquals(LocalDate.of(2023, 10, 3), staff.getBirthDate());
        assertEquals(LocalDate.of(2025, 3, 10), staff.getHireDate());
        assertEquals(20_000, staff.getSalary(), 0.001);
        assertEquals(ContractTypeEnum.FULL_TIME, staff.getContractType());
        assertTrue(staff.getManager().isEmpty());
    }

    @Test
    public void constructor_WithValidParametersAndValidManagerField_ShouldCreateStaff() {
        Staff m = new Staff("Test first1", "Test last1", "1234567",
                "example1@gmail.com", "pass1", LocalDate.of(2023, 10, 3),
                LocalDate.of(2025, 3, 10), 20_000, ContractTypeEnum.FULL_TIME,
                null);
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2025, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, m);

        assertEquals("Test first", staff.getFirstName());
        assertEquals("Test last", staff.getLastName());
        assertEquals("1234567", staff.getPhoneNumber().get());
        assertEquals("example@gmail.com", staff.getEmail());
        assertEquals("pass", staff.getPassword());
        assertEquals(LocalDate.of(2023, 10, 3), staff.getBirthDate());
        assertEquals(LocalDate.of(2025, 3, 10), staff.getHireDate());
        assertEquals(20_000, staff.getSalary(), 0.001);
        assertEquals(ContractTypeEnum.FULL_TIME, staff.getContractType());
        assertNotNull(staff.getManager().get());

        Staff manager = staff.getManager().get();
        assertEquals("Test first1", manager.getFirstName());
        assertEquals("Test last1", manager.getLastName());
        assertEquals("1234567", manager.getPhoneNumber().get());
        assertEquals("example1@gmail.com", manager.getEmail());
        assertEquals("pass1", manager.getPassword());
        assertEquals(LocalDate.of(2023, 10, 3), manager.getBirthDate());
        assertEquals(LocalDate.of(2025, 3, 10), manager.getHireDate());
        assertEquals(20_000, manager.getSalary(), 0.001);
        assertEquals(ContractTypeEnum.FULL_TIME, manager.getContractType());
    }

    @Test
    public void constructor_WithFutureHireDateField_ShouldThrowDateInFutureException() {
        DateInFutureException exception = assertThrows(DateInFutureException.class, () -> {
            new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2027, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Hire Date can not be in the future", exception.getMessage());
    }

    @Test
    public void constructor_WithNullHireDateField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    null, 20_000, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Hire Date can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroSalaryField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 0, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Salary must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeSalaryField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), -20, ContractTypeEnum.FULL_TIME, null);
        });
        assertEquals("Salary must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithNullContractTypeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20.3, null, null);
        });
        assertEquals("Contract Type can not be null", exception.getMessage());
    }

    @Test
    public void setHireDate_WithFutureHireDateParameter_ShouldThrowDateInFutureException() {
        DateInFutureException exception = assertThrows(DateInFutureException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2027, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setHireDate(LocalDate.of(2027, 10, 3));
        });
        assertEquals("Hire Date can not be in the future", exception.getMessage());
    }

    @Test
    public void setHireDate_WithNullHireDateParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setHireDate(null);
        });
        assertEquals("Hire Date can not be null", exception.getMessage());
    }

    @Test
    public void setHireDate_WithValidHireDateParameter_ShouldChangeHireDate() {
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.PART_TIME, null);
        staff.setHireDate(LocalDate.of(2022, 10, 3));

        assertEquals(LocalDate.of(2022, 10, 3), staff.getHireDate());
    }

    @Test
    public void setSalary_WithZeroSalaryParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setSalary(0);
        });
        assertEquals("Salary must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setSalary_WithNegativeSalaryParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setSalary(-29.6);
        });
        assertEquals("Salary must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setSalary_WithValidSalaryParameter_ShouldChangeSalary() {
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.PART_TIME, null);
        staff.setSalary(10_000);

        assertEquals(10_000, staff.getSalary(), 0.001);
    }

    @Test
    public void setContractType_WithNullContractTypeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setContractType(null);
        });
        assertEquals("Contract Type can not be null", exception.getMessage());
    }

    @Test
    public void setContractType_WithValidContractTypeParameter_ShouldChangeContractType() {
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.PART_TIME, null);
        staff.setContractType(ContractTypeEnum.FULL_TIME);

        assertEquals(ContractTypeEnum.FULL_TIME, staff.getContractType());
    }

    @Test
    public void setManager_WithRecursiveReference_ShouldThrowRecursionException() {
        RecursionException exception = assertThrows(RecursionException.class, () -> {
            Staff staff = new Staff("Test first", "Test last", "1234567",
                    "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
            staff.setManager(staff);
        });
        assertEquals("An object cannot have a recursive association with itself", exception.getMessage());
    }

    @Test
    public void setManager_WithValidParameter_ShouldThrowRecursionException() {
        Staff m = new Staff("Test first1", "Test last1", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
        Staff staff = new Staff("Test first", "Test last", "1234567",
                "example@gmail.com", "pass", LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 3, 10), 20_000, ContractTypeEnum.FULL_TIME, null);
        staff.setManager(m);

        Staff manager = staff.getManager().get();
        assertEquals("Test first1", manager.getFirstName());
        assertEquals("Test last1", manager.getLastName());
        assertEquals("1234567", manager.getPhoneNumber().get());
        assertEquals("example@gmail.com", manager.getEmail());
        assertEquals("pass", manager.getPassword());
        assertEquals(LocalDate.of(2023, 10, 3), manager.getBirthDate());
        assertEquals(LocalDate.of(2023, 3, 10), manager.getHireDate());
        assertEquals(20_000, manager.getSalary(), 0.001);
        assertEquals(ContractTypeEnum.FULL_TIME, manager.getContractType());
    }
}
