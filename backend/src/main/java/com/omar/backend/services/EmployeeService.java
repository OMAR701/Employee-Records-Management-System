package com.omar.backend.services;


import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.mapper.EmployeeMapper;
import com.omar.backend.models.Employee;
import com.omar.backend.models.EmployeeAudit;
import com.omar.backend.models.UserEntity;
import com.omar.backend.repositories.EmployeeAuditRepository;
import com.omar.backend.repositories.EmployeeRepository;
import com.omar.backend.utils.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.omar.backend.utils.UserUtils.getAuthenticatedUserRole;
import static com.omar.backend.utils.UserUtils.getUserDepartment;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;
    private final EmployeeAuditRepository auditRepository;


    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, UserService userService, EmployeeAuditRepository auditRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.userService = userService;
        this.auditRepository = auditRepository;
    }

    public Employee createEmployee(EmployeeRequest request) {
        UserEntity currentUser = userService.getCurrentUser();
        String changes = "created new employee : ...";
        Employee employee = Employee.builder()
                .fullName(request.fullName())
                .jobTitle(request.jobTitle())
                .department(request.department())
                .hireDate(request.hireDate())
                .employmentStatus(request.employmentStatus())
                .contactInfo(request.contactInfo())
                .address(request.address())
                .createdBy(currentUser.getUsername())
                .build();

        logAudit(employee.getEmployeeId(), "CREATED", currentUser.getUsername(), changes);

        return employeeRepository.save(employee);
    }


    public List<EmployeeRequest> getEmployeesForCurrentUser() {
        UserEntity currentUser = userService.getCurrentUser();

        if (currentUser.getRoles().contains("MANAGER")) {
            String managerDepartment = currentUser.getDepartment();
            return getEmployeesByDepartment(managerDepartment);
        }

        return getAllEmployees();
    }

    public List<EmployeeRequest> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department)
                .stream()
                .map(employeeMapper::toRequest)
                .collect(Collectors.toList());
    }


    public List<EmployeeRequest> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toRequest)
                .collect(Collectors.toList());
    }

    public Employee updateEmployee(Integer id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        UserEntity currentUser = userService.getCurrentUser();
        String changes = "Updated fields: ...";
        if (currentUser.getRoles().contains("ROLE_MANAGER") &&
                !existingEmployee.getDepartment().equals(currentUser.getDepartment())) {
            throw new AccessDeniedException("You do not have permission to update this employee.");
        }

        existingEmployee.setFullName(request.fullName());
        existingEmployee.setJobTitle(request.jobTitle());
        existingEmployee.setDepartment(request.department());
        existingEmployee.setHireDate(request.hireDate());
        existingEmployee.setEmploymentStatus(request.employmentStatus());
        existingEmployee.setContactInfo(request.contactInfo());
        existingEmployee.setAddress(request.address());

        logAudit(existingEmployee.getEmployeeId(), "UPDATE", currentUser.getUsername(), changes);
        return employeeRepository.save(existingEmployee);
    }




    public EmployeeRequest getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        return employeeMapper.toRequest(employee);
    }

    public void deleteEmployee(Integer id) {
        UserEntity currentUser = userService.getCurrentUser();

        String changes = "Delete Employee";
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        logAudit(employee.getEmployeeId(), "Detele", currentUser.getUsername(), changes);

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


    public void logAudit(Integer employeeId, String action, String performedBy, String changes) {
        EmployeeAudit audit = EmployeeAudit.builder()
                .employeeId(employeeId)
                .action(action)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .changes(changes)
                .build();

        auditRepository.save(audit);
    }
}