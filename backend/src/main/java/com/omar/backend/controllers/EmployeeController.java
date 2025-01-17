package com.omar.backend.controllers;

import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.models.Employee;
import com.omar.backend.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.base-path}/employees")
@Tag(name = "Employee Management", description = "Manage employees in the system")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create a new employee", description = "Allows admin to create a new employee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee createdEmployee = employeeService.createEmployee(request);
        return ResponseEntity.ok(createdEmployee);
    }

    @Operation(summary = "Retrieve all employees", description = "Fetch all employees, accessible by Admin, HR, and Manager roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR', 'ROLE_MANAGER')")
    @GetMapping("/list")
    public ResponseEntity<List<EmployeeRequest>> getAllEmployees() {
        List<EmployeeRequest> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Retrieve employee by ID", description = "Fetch an employee by ID, accessible by Admin, HR, and Manager roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR', 'ROLE_MANAGER')")
    @GetMapping("/details/{id}")
    public ResponseEntity<EmployeeRequest> getEmployeeById(@PathVariable Integer id) {
        EmployeeRequest employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Update Employee", description = "Update an employee's details by ID. Accessible to Admin, HR, and Manager roles.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR', 'ROLE_MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeRequest request) {
        Employee updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(summary = "Delete Employee", description = "Delete an employee by ID. Only accessible to HR.")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search employees by name or job title", description = "Allows searching employees by full name or job title.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR', 'ROLE_MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeRequest>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String jobTitle
    ) {
        List<EmployeeRequest> employees = employeeService.searchEmployees(name, jobTitle);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Filter employees", description = "Filter employees by department, employment status, or hire date.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR', 'ROLE_MANAGER')")
    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeRequest>> filterEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String employmentStatus,
            @RequestParam(required = false) LocalDate hireDate
    ) {
        List<EmployeeRequest> employees = employeeService.filterEmployees(department, employmentStatus, hireDate);
        return ResponseEntity.ok(employees);
    }
}
