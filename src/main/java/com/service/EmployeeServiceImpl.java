package com.service;

import com.model.Employee;
import com.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll().stream().sorted(((o1, o2) -> (int) (o1.getId() - o2.getId()))).collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.getOne(id);
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee editEmployeeById(Long id, int departmentId, String jobTitle) {
        Employee employee = employeeRepository.getOne(id);
        employee.setDepartmentId(departmentId);
        employee.setJobTitle(jobTitle);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
