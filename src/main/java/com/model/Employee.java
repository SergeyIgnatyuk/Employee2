package com.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.util.CustomDateConstraint;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "employees")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "First name of birth must be not empty")
    @Pattern(regexp = "[^\\s]*", message = "First name must have not whitespaces")
    @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name of birth must be not empty")
    @Pattern(regexp = "[^\\s]*", message = "Last name must have not whitespaces")
    @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters")
    private String lastName;

    @Column(name = "department_id")
    @NotNull(message = "Department ID of birth must be not empty")
    @Min(value = 1, message = "Department ID must between 1 and 2")
    @Max(value = 2, message = "Department ID must between 1 and 2")
    private int departmentId;

    @Column(name = "job_title")
    @NotEmpty(message = "Job title of birth must be not empty")
    @Size(min = 2, max = 25, message = "Job title must be between 3 and 25 characters")
    private String jobTitle;

    @Column(name = "gender")
    @NotEmpty(message = "Gender of birth must be not empty")
    @Pattern(regexp = "male|Male|female|Female", message = "Gender must be male or female")
    private String gender;

    @Column(name = "date_of_birth")
    @NotNull(message = "Date of birth must be not empty")
    @CustomDateConstraint
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
}
