package com.omar.backend.services;


import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.mapper.EmployeeMapper;
import com.omar.backend.models.Employee;
import com.omar.backend.repositories.EmployeeRepository;
import com.omar.backend.utils.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.omar.backend.utils.UserUtils.getAuthenticatedUserRole;
import static com.omar.backend.utils.UserUtils.getUserDepartment;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public Employee createEmployee(EmployeeRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        return employeeRepository.save(employee);
    }

    public List<EmployeeRequest> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toRequest)
                .collect(Collectors.toList());
    }

    public Employee updateEmployee(Integer id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        String role = UserUtils.getAuthenticatedUserRole();
        String username = UserUtils.getAuthenticatedUsername();

        if ("ROLE_MANAGER".equals(role)) {
            if (!existingEmployee.getDepartment().equals(UserUtils.getUserDepartment(username))) {
                throw new AccessDeniedException("Managers can only update employees in their department.");
            }
            existingEmployee.setJobTitle(request.jobTitle());
            existingEmployee.setContactInfo(request.contactInfo());
        } else if ("ROLE_HR".equals(role) || "ROLE_ADMIN".equals(role)) {
            existingEmployee.setFullName(request.fullName());
            existingEmployee.setJobTitle(request.jobTitle());
            existingEmployee.setDepartment(request.department());
            existingEmployee.setHireDate(request.hireDate());
            existingEmployee.setEmploymentStatus(request.employmentStatus());
            existingEmployee.setContactInfo(request.contactInfo());
            existingEmployee.setAddress(request.address());
        } else {
            throw new AccessDeniedException("Access denied.");
        }

        return employeeRepository.save(existingEmployee);
    }



    public EmployeeRequest getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        return employeeMapper.toRequest(employee);
    }

    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        employeeRepository.delete(employee);
    }


    public List<EmployeeRequest> searchEmployees(Integer id, String name, String department, String jobTitle) {
        List<Employee> employees;

        if (id != null) {
            employees = employeeRepository.findById(id).stream().toList();
        } else if (name != null && department != null && jobTitle != null) {
            employees = employeeRepository.findByFullNameContainingIgnoreCaseAndDepartmentContainingIgnoreCaseAndJobTitleContainingIgnoreCase(name, department, jobTitle);
        } else if (name != null && department != null) {
            employees = employeeRepository.findByFullNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(name, department);
        } else if (name != null && jobTitle != null) {
            employees = employeeRepository.findByFullNameContainingIgnoreCaseAndJobTitleContainingIgnoreCase(name, jobTitle);
        } else if (department != null && jobTitle != null) {
            employees = employeeRepository.findByDepartmentContainingIgnoreCaseAndJobTitleContainingIgnoreCase(department, jobTitle);
        } else if (name != null) {
            employees = employeeRepository.findByFullNameContainingIgnoreCase(name);
        } else if (department != null) {
            employees = employeeRepository.findByDepartmentContainingIgnoreCase(department);
        } else if (jobTitle != null) {
            employees = employeeRepository.findByJobTitleContainingIgnoreCase(jobTitle);
        } else {
            employees = Collections.emptyList();
        }

        return employees.stream()
                .map(employeeMapper::toRequest)
                .collect(Collectors.toList());
    }


    public List<EmployeeRequest> filterEmployees(String department, String employmentStatus, LocalDate hireDate) {
        List<Employee> employees = employeeRepository.filterEmployees(department, employmentStatus, hireDate);

        return employees.stream()
                .map(employeeMapper::toRequest)
                .collect(Collectors.toList());
    }
}