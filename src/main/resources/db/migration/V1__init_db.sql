-- Table: employees
create table employees
(
    id            int8 generated by default as identity,
    first_name    varchar(15),
    last_name     varchar(15),
    department_id int4      not null check (department_id >= 1 AND department_id <= 2),
    job_title     varchar(25),
    gender        varchar(255),
    date_of_birth timestamp not null,
    primary key (id)
)