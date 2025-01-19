package com.omar.backend.repositories;

import com.omar.backend.models.EmployeeAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAuditRepository extends JpaRepository<EmployeeAudit, Long> {
}
