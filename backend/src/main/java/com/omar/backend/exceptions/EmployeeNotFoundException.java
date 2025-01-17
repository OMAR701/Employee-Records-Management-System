package com.omar.backend.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    private final Integer employeeId;

    public EmployeeNotFoundException(Integer employeeId, String message) {
        super(message);
        this.employeeId = employeeId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }
}
