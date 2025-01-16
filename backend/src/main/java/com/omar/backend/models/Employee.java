package com.omar.backend.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String employeeId;

    private String jobTitle;
    private String department;
    private LocalDate hireDate;
    private String employmentStatus;
    private String contactInfo;
    private String address;


}
