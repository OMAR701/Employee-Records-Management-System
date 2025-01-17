package com.omar.backend.mapper;

import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .fullName(request.fullName())
                .jobTitle(request.jobTitle())
                .department(request.department())
                .hireDate(request.hireDate())
                .employmentStatus(request.employmentStatus())
                .contactInfo(request.contactInfo())
                .address(request.address())
                .build();
    }

    public EmployeeRequest toRequest(Employee entity) {
        return new EmployeeRequest(
                entity.getEmployeeId(),
                entity.getFullName(),
                entity.getJobTitle(),
                entity.getDepartment(),
                entity.getHireDate(),
                entity.getEmploymentStatus(),
                entity.getContactInfo(),
                entity.getAddress()
        );
    }
}
