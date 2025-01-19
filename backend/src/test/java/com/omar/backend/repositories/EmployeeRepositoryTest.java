package com.omar.backend.repositories;

import com.omar.backend.models.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Transactional
    @Test
    public void testFindAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    public void testFindByDepartment() {
        List<Employee> employees = employeeRepository.findByDepartment("Nonexistent");
        assertTrue(employees.isEmpty());
    }

    @Test
    public void testFindByJobTitle() {
        List<Employee> employees = employeeRepository.findByJobTitle("Developer");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }

    @Test
    public void testFindByFullNameContainingIgnoreCase() {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase("john");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }
}
