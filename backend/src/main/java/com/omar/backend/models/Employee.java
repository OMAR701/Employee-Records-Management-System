package com.omar.backend.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employees")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(nullable = false)
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    private String jobTitle;
    private String department;
    private LocalDate hireDate;
    private String employmentStatus;
    private String contactInfo;
    private String address;


}
