package com.service;

import com.model.Employee;

import java.util.List;

/**
 * Service interface for {@link com.model.Employee}
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

public interface EmployeeService {
    List<Employee> getEmployees();

    Employee getEmployeeById(Long id);

    void addEmployee(Employee employee);

    Employee editEmployeeById(Long id, int departmentId, String jobTitle);

    void deleteEmployeeById(Long id);
}
