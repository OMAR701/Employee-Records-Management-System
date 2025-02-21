-- Insert users
INSERT INTO users (id, username, password, department)
VALUES (1, 'admin', '$2a$10$egsLaUVYqh8BARpsCv0Zr.dydDfVKaC2eaMgsse5R2g7wCKLyUmwq','ADMINISTRATION');

INSERT INTO users (id, username, password, department)
VALUES (2, 'hr', '$2a$10$9.GnZrE961mCRW2ztqvLB.H2dsDJtaJV1DJsICTtZRfYP4ryBu04u','HR');

INSERT INTO users (id, username, password, department)
VALUES (3, 'manager', '$2a$10$czZtbMBErkpwV4Kb0lbbSuGXxqCh3q3hKkmTlpTAEgWfbEt2qqlA6','IT');

-- Insert user roles
INSERT INTO user_roles (user_id, role)
VALUES (1, 'ADMIN');

INSERT INTO user_roles (user_id, role)
VALUES (2, 'HR');

INSERT INTO user_roles (user_id, role)
VALUES (3, 'MANAGER');

-- Insert employees
INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address)
VALUES (DEFAULT, 'John Doe', 'Developer', 'IT', TO_DATE('2022-01-15', 'YYYY-MM-DD'), 'Full-time', 'john.doe@example.com', '123 Main St');

INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address)
VALUES (DEFAULT, 'Jane Smith', 'Manager', 'HR', TO_DATE('2021-12-01', 'YYYY-MM-DD'), 'Part-time', 'jane.smith@example.com', '456 Maple Ave');

INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address)
VALUES (DEFAULT, 'Alice Johnson', 'Designer', 'Marketing', TO_DATE('2023-03-10', 'YYYY-MM-DD'), 'Contractor', 'alice.johnson@example.com', '789 Oak Dr');

INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address)
VALUES (DEFAULT, 'Mark Johnson', 'Sales Manager', 'Sales', TO_DATE('2019-03-10', 'YYYY-MM-DD'), 'Active', 'mark.johnson@example.com', '789 Pine St');
