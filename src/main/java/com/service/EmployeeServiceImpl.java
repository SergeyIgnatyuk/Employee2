package com.service;

import com.error.ResourceNotFoundException;
import com.model.Employee;
import com.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

/**
 * Implementation of {@link EmployeeService} interface.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JmsProducerService jmsProducerService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, JmsProducerService jmsProducerService) {
        this.employeeRepository = employeeRepository;
        this.jmsProducerService = jmsProducerService;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepository.findAll().stream().sorted(((o1, o2) -> (int) (o1.getId() - o2.getId()))).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " Not Found!"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
        jmsProducerService.sendMessage(String.format("Employee %s %s created!", employee.getFirstName(), employee.getLastName()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public Employee editEmployeeById(Long id, int departmentId, String jobTitle) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " Not Found!"));
        employee.setDepartmentId(departmentId);
        employee.setJobTitle(jobTitle);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " Not Found!"));
        employeeRepository.delete(employee);
    }
}
