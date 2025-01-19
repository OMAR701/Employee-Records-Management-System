Employee Records Management System

Project Overview

The Employee Records Management System is a Spring Boot application designed to manage employee data efficiently. It supports CRUD operations (Create, Read, Update, Delete) for employees and includes role-based access control for HR personnel, managers, and administrators.

Features

Role-based access:

HR: Full CRUD access to all employees.

Manager: Limited update access for employees within their department.

Administrator: Full system access, including managing user permissions and system settings.

JWT-based authentication for secure access.

Swagger UI for API documentation.

Audit trail for tracking changes to employee records.

Technologies Used

Backend: Spring Boot, Spring Security, Hibernate

Database: Oracle Database

Authentication: JWT (JSON Web Tokens)

API Documentation: Swagger/OpenAPI

Frontend (Optional): Swing-based desktop UI

Setup and Installation

Prerequisites

Ensure you have the following installed:

Java 17+

Oracle Database (configured with the xe instance)

Maven

Database Configuration

Set up your Oracle Database with the following connection details:

URL: jdbc:oracle:thin:@localhost:1521:xe

Username: system

Password: 12345

Run the data.sql script located in the resources folder to populate initial data.

Application Configuration

The application configuration is managed via the application.yml file. Update the following fields if needed:

spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: system
    password: 12345
  jpa:
    hibernate:
      ddl-auto: create

Build and Run

Clone the repository:

git clone https://github.com/OMAR701/EmployeeRecordsManagementSystem.git
cd EmployeeRecordsManagementSystem

Build the project using Maven:

mvn clean install

Run the application:

mvn spring-boot:run

Access the Swagger UI for API documentation:

URL: http://localhost:8080/swagger-ui/index.html

User Roles and Permissions

Role

Permissions

Administrator

Full access to all features and configurations

HR

CRUD operations on all employee records

Manager

View and update limited employee data within the department

API Endpoints

Authentication

Login: POST /api/v1/auth/login

Employee Management

Create Employee: POST /api/v1/employees/create (Admin only)

Get All Employees: GET /api/v1/employees/list

Get Employee by ID: GET /api/v1/employees/details/{id}

Update Employee: PUT /api/v1/employees/update/{id}

Delete Employee: DELETE /api/v1/employees/delete/{id} (HR only)

Contact

For any issues or inquiries, reach out to:

Name: Omar

GitHub: OMAR701

Email: kader.omar.2001@gmail.com

admin : username = admin  ; password= admin123
hr: username = hr;  password= hr123
manager : username = manager; password = manager123

