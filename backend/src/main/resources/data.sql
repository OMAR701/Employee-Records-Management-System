-- Seed initial data for roles and users
INSERT INTO users (id, username, password, roles) VALUES
                                                      (1, 'admin', '$2a$10$E6sUYtS/eVYhUybdci5Tpe98loEYbZ4PTpU9vlT/X4qjTL6ubwAKK', 'ROLE_ADMIN');
                                                      (2, 'hr', '$2a$10$4HDg1ZhdcvHewS8eK3u5yuqV5hn.FZDJNJ0WkwVWDB3RYHR1O5DOa', 'ROLE_HR'),
                                                      (3, 'manager', '$2a$10$JxH3hheTz0/6ay12fu/xw.zupOpmuBQZMjNHEjSowR7jYvBFRIqDy', 'ROLE_MANAGER');

-- Seed initial employees
INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address) VALUES
                                                                                                                               (1, 'John Doe', 'Software Engineer', 'IT', '2021-01-15', 'Active', 'john.doe@example.com', '123 Main St, City'),
                                                                                                                               (2, 'Jane Smith', 'HR Manager', 'HR', '2018-06-25', 'Active', 'jane.smith@example.com', '456 Elm St, City'),
                                                                                                                               (3, 'Mark Johnson', 'Sales Manager', 'Sales', '2019-03-10', 'Active', 'mark.johnson@example.com', '789 Pine St, City');
