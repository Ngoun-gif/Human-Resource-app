CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,

    employee_code VARCHAR(20) NOT NULL UNIQUE,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    date_of_birth DATE,

    email VARCHAR(150),
    phone VARCHAR(20),
    address TEXT,
    photo_url VARCHAR(500),
    photo_file_id VARCHAR(255),

    department_id BIGINT,
    position_id BIGINT,

    hire_date DATE,
    salary DECIMAL(15,2),
    status VARCHAR(20) NOT NULL,

    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id),

    CONSTRAINT fk_employee_position
        FOREIGN KEY (position_id)
        REFERENCES positions(id)
);