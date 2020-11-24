package com.controller;

import com.model.Employee;
import com.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Controller for {@link com.model.Employee}'s pages.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RestController
@Validated
@Api(value = "employee resources", description = "APIs for working with employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @ApiOperation(value = "get all employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
    }

    @GetMapping("/employees/{id}")
    @ApiOperation(value = "get employee by ID")
    public ResponseEntity<Object> getEmployeeById(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "create new employee")
    public ResponseEntity<HttpHeaders> addEmployee(@Valid @RequestBody Employee employee, UriComponentsBuilder uriComponentsBuilder) {
        employeeService.addEmployee(employee);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/employees/{id}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{id}")
    @ApiOperation(value = "update department & job employee by ID")
    public ResponseEntity<Employee> editEmployeeById(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id,
                                                     @Min(value = 1, message = "must between 1 and 2") @Max(value = 2, message = "must between 1 and 2") @RequestParam int departmentId,
                                                     @RequestParam @Size(min = 2, max = 25, message = "must be between 3 and 25 characters") String jobTitle) {
        return new ResponseEntity<>(employeeService.editEmployeeById(id, departmentId, jobTitle), HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}")
    @ApiOperation(value = "delete employee by ID")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
