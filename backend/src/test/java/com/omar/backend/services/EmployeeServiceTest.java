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
    public void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");
        Employee employee = new Employee();

        when(employeeMapper.toEntity(request)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("Developer", result.getJobTitle());
        verify(employeeMapper, times(1)).toEntity(request);
        verify(employeeRepository, times(1)).save(any(Employee.class));
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

    @Test
    public void testUpdateEmployee() {
        Employee existingEmployee = new Employee();
        EmployeeRequest updatedRequest = new EmployeeRequest(null, "John Doe Updated", "Senior Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");
        Employee updatedEmployee = new Employee();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        when(employeeMapper.toEntity(updatedRequest)).thenReturn(updatedEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1, updatedRequest);

        assertNotNull(result);
        assertEquals("John Doe Updated", result.getFullName());
        assertEquals("Senior Developer", result.getJobTitle());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).delete(employee);
    }
}
