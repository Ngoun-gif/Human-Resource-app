CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    hire_date DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    department_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    employee_type_id BIGINT NOT NULL,
    user_id BIGINT,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_employees_department
    FOREIGN KEY (department_id)
    REFERENCES hr_management.departments(id),

    CONSTRAINT fk_employees_position
    FOREIGN KEY (position_id)
    REFERENCES hr_management.positions(id),

    CONSTRAINT fk_employees_employee_type
    FOREIGN KEY (employee_type_id)
    REFERENCES hr_management.employee_types(id),

    CONSTRAINT fk_employees_user
    FOREIGN KEY (user_id)
    REFERENCES hr_management.users(id)
    );