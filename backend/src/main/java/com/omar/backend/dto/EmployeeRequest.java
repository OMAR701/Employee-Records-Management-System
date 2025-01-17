package com.omar.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@JsonInclude(Include.NON_EMPTY)
public record EmployeeRequest(
        @NotNull(message = "Employee ID must be provided")
        Integer employeeId,

        @NotBlank(message = "Full name must not be blank")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,

        @NotBlank(message = "Job title must not be blank")
        String jobTitle,

        @NotBlank(message = "Department must not be blank")
        String department,

        @NotNull(message = "Hire date must be provided")
        @PastOrPresent(message = "Hire date must be in the past or present")
        LocalDate hireDate,

        @NotBlank(message = "Employment status must not be blank")
        String employmentStatus,

        @NotBlank(message = "Contact information must not be blank")
        @Email(message = "Contact information must be a valid email")
        String contactInfo,

        @NotBlank(message = "Address must not be blank")
        String address
) {}