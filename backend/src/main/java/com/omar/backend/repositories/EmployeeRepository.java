package com.omar.backend.repositories;

import com.omar.backend.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByDepartment(String department);

    List<Employee> findByFullNameContainingIgnoreCase(String fullName);

    List<Employee> findByJobTitle(String jobTitle);


    List<Employee> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<Employee> findByFullNameContainingIgnoreCaseAndJobTitleContainingIgnoreCase(String fullName, String jobTitle);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:employmentStatus IS NULL OR e.employmentStatus = :employmentStatus) AND " +
            "(:hireDate IS NULL OR e.hireDate = :hireDate)")
    List<Employee> filterEmployees(
            @Param("department") String department,
            @Param("employmentStatus") String employmentStatus,
            @Param("hireDate") LocalDate hireDate
    );


}
