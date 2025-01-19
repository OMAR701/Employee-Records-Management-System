package com.omar.backend.integration;

import com.omar.backend.models.Employee;
import com.omar.backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    public void testFindAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    public void testFindByDepartment() {
        List<Employee> employees = employeeRepository.findByDepartment("IT");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }

    @Test
    public void testFindByFullNameContainingIgnoreCase() {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase("john");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }

    @Test
    public void testFindByJobTitle() {
        List<Employee> employees = employeeRepository.findByJobTitle("Developer");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }


    @Test
    public void testSearchEmployeesByName() {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase("john");
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }

    @Test
    public void testFilterEmployees() {
        List<Employee> employees = employeeRepository.filterEmployees("IT", "Full-time", null);
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getFullName());
    }


}
