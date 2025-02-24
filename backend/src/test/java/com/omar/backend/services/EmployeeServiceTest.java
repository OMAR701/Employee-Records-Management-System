package com.omar.backend.services;

import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.mapper.EmployeeMapper;
import com.omar.backend.models.Employee;
import com.omar.backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }



    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        when(employeeMapper.toRequest(employee1)).thenReturn(new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street"));
        when(employeeMapper.toRequest(employee2)).thenReturn(new EmployeeRequest(null, "Jane Smith", "Manager", "HR", LocalDate.now(), "Part-time", "jane.smith@example.com", "456 Avenue"));

        List<EmployeeRequest> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).fullName());
        assertEquals("Jane Smith", employees.get(1).fullName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toRequest(employee)).thenReturn(new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street"));

        EmployeeRequest result = employeeService.getEmployeeById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.fullName());
        verify(employeeRepository, times(1)).findById(1);
    }




}
