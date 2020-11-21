package com.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.valid.CustomDateConstraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Simple JavaBean domain object that represents a Employee.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ApiModel(value = "employee class")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID of employee", example = "1")
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "{employee.firstName.noEmpty}")
    @Pattern(regexp = "[^\\s]*", message = "{employee.firstName.pattern}")
    @Size(min = 3, max = 15, message = "{employee.firstName.length}")
    @ApiModelProperty(value = "first name of employee", example = "Sergey")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "{employee.lastName.noEmpty}")
    @Pattern(regexp = "[^\\s]*", message = "{employee.lastName.pattern}")
    @Size(min = 3, max = 15, message = "{employee.lastName.length}")
    @ApiModelProperty(value = "last name of employee", example = "Ignatyuk")
    private String lastName;

    @Column(name = "department_id")
    @NotNull(message = "{employee.departmentId.notNull}")
    @Min(value = 1, message = "{employee.departmentId.length}")
    @Max(value = 2, message = "{employee.departmentId.length}")
    @ApiModelProperty(value = "department ID of employee", example = "1")
    private int departmentId;

    @Column(name = "job_title")
    @NotEmpty(message = "Job title of birth must be not empty")
    @Size(min = 2, max = 25, message = "Job title must be between 3 and 25 characters")
    @ApiModelProperty(value = "job title of employee", example = "java developer")
    private String jobTitle;

    @Column(name = "gender")
    @NotEmpty(message = "{employee.gender.noEmpty}")
    @Pattern(regexp = "male|Male|female|Female", message = "{employee.gender.pattern}")
    @ApiModelProperty(value = "gender of employee", example = "male")
    private String gender;

    @Column(name = "date_of_birth")
    @NotNull(message = "{employee.dateOfBirth.notNull}")
    @CustomDateConstraint
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "date of birth of employee", example = "1989-08-07")
    private Date dateOfBirth;
}
