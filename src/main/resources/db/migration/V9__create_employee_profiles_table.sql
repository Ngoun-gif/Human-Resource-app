CREATE TABLE employee_profiles (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL UNIQUE,
    photo_file_id VARCHAR(255),
    photo_file_name VARCHAR(255),
    photo_url VARCHAR(500),
    photo_mime_type VARCHAR(100),
    photo_size BIGINT,

    date_of_birth DATE,
    address VARCHAR(255),
    emergency_contact_name VARCHAR(150),
    emergency_contact_phone VARCHAR(30),
    notes VARCHAR(500),

    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_employee_profiles_employee
    FOREIGN KEY (employee_id)
    REFERENCES hr_management.employees(id)
    ON DELETE CASCADE
    );