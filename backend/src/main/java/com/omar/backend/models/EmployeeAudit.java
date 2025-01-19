package com.omar.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee_audit")
public class EmployeeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer employeeId;
    private String action;
    private String performedBy;
    private LocalDateTime performedAt;
    private String changes;
}

