package com.service;

import com.model.Employee;
import com.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Integrations tests of {@link EmployeeServiceImpl}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RunWith(SpringRunner.class)
public class EmployeeServiceIntegrationTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private List<Employee> employeeList;


    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @MockBean
        private EmployeeRepository employeeRepository;

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl(employeeRepository);
        }
    }

    @Before
    public void setUp() {
        this.employee = Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime())
                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Natalia")
                .lastName("Sergeeva")
                .departmentId(2)
                .jobTitle("Java Developer")
                .gender("female")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime())
                .build();

        this.employeeList = Stream.of(employee2, employee).collect(Collectors.toList());

        Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
    }

    @Test
    public void whenGetEmployees_thenMethodFindAllShouldBeCalled() {
        this.employeeList = employeeService.getEmployees();

        Assert.assertEquals(1, employeeList.get(0).getId().intValue());
        Assert.assertEquals(2, employeeList.get(1).getId().intValue());
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void whenGetEmployeeById_thenMethodFindByIdShouldBeCalled() {
        this.employee = employeeService.getEmployeeById(1L);

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals("Sergey", employee.getFirstName());
        Assert.assertEquals("Sergeev", employee.getLastName());
        Assert.assertEquals(1, employee.getDepartmentId());
        Assert.assertEquals("QA", employee.getJobTitle());
        Assert.assertEquals("male", employee.getGender());
        Assert.assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employee.getDateOfBirth());
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void whenAddEmployee_thenMethodSaveShouldBeCalled() {
        employeeService.addEmployee(employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void whenEditEmployeeById_thenMethodFindByIdAndSaveShouldBeCalled() {
        employeeService.editEmployeeById(1L, 2, "Java Developer");

        Assert.assertEquals(2, employee.getDepartmentId());
        Assert.assertEquals("Java Developer", employee.getJobTitle());
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void whenDeleteEmployeeById_thenMethodDeleteShouldBeCalled() {
        employeeService.deleteEmployeeById(1L);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(employeeRepository, Mockito.times(1)).delete(employee);
    }
}
