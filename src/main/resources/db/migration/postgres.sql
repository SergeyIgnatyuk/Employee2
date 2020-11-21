DROP TABLE if exists user_roles, users, roles, employees;
-- Table: users
CREATE TABLE users
(
    user_id  INT GENERATED ALWAYS AS identity PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

-- Table: roles
CREATE TABLE roles
(
    role_id INT GENERATED ALWAYS AS identity PRIMARY KEY,
    name    TEXT NOT NULL UNIQUE
);

-- Table for mapping user and roles: user_roles
CREATE TABLE user_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id),

    UNIQUE (user_id, role_id)
);

-- Table: employees
CREATE TABLE employees
(
    id  INT GENERATED ALWAYS AS identity PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    department_id INT NOT NULL,
    job_title TEXT NOT NULL,
    gender TEXT NOT NULL,
    date_of_birth DATE NOT NULL
);

-- Insert data
INSERT INTO users (username, password)
VALUES ('admin', '$2a$11$uSXS6rLJ91WjgOHhEGDx..VGs7MkKZV68Lv5r1uwFu7HgtRn3dcXG');

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (name)
VALUES ('ROLE_USER');

INSERT INTO user_roles
VALUES (1, 1);

INSERT INTO employees (first_name, last_name, department_id, job_title, gender, date_of_birth)
VALUES ('Sergey', 'Sergeev', 1, 'Java Developer', 'male', '1989-08-07');
INSERT INTO employees (first_name, last_name, department_id, job_title, gender, date_of_birth)
VALUES ('Natasha', 'Sergeeva', 2, 'QA', 'female', '1989-04-04');