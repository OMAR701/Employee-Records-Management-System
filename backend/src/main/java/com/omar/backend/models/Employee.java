package com.omar.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


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

    @Column(updatable = false)
    @JsonIgnore
    private String createdBy;

    @Column(updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private String updatedBy;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
