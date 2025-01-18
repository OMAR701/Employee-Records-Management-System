package com.omar.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {

    @JsonProperty("employeeId")
    private int id;
    private String fullName;
    private String jobTitle;
    private String department;
    private String email;
    private String employmentStatus;
    private String address;
    private String hireDate;
    private String contactInfo;

    public Employee() {
    }

    public Employee(int id, String fullName, String jobTitle, String department, String email, String employmentStatus, String address, String hireDate, String contactInfo) {
        this.id = id;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.department = department;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.address = address;
        this.hireDate = hireDate;
        this.contactInfo = contactInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", address='" + address + '\'' +
                ", hireDate='" + hireDate + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
