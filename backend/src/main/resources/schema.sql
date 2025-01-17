/*CREATE TABLE users (
                       id NUMBER PRIMARY KEY,
                       username VARCHAR2(50) NOT NULL,
                       password VARCHAR2(255) NOT NULL
);

CREATE TABLE user_roles (
                            user_id NUMBER NOT NULL,
                            role VARCHAR2(50) NOT NULL,
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE employees (
                           employee_id NUMBER PRIMARY KEY,
                           full_name VARCHAR2(100) NOT NULL,
                           job_title VARCHAR2(100),
                           department VARCHAR2(100),
                           hire_date DATE,
                           employment_status VARCHAR2(50),
                           contact_info VARCHAR2(100),
                           address VARCHAR2(255)
);
*/