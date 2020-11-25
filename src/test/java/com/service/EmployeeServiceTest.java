package com.service;

import com.error.ResourceNotFoundException;
import com.model.Employee;
import com.repository.EmployeeRepository;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Integrations tests of {@link EmployeeServiceImpl}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RunWith(SpringRunner.class)
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JmsProducerService jmsProducerService;

    private Employee employee;
    private List<Employee> employeeList;


    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @MockBean
        private EmployeeRepository employeeRepository;
        @MockBean
        private JmsProducerService jmsProducerService;

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl(employeeRepository, jmsProducerService);
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

        when(employeeRepository.findAll()).thenReturn(employeeList);
        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
    }

    @Test
    public void whenGetEmployees_thenMethodFindAllShouldBeCalled() {
        this.employeeList = employeeService.getEmployees();

        assertEquals(1, employeeList.get(0).getId().intValue());
        assertEquals(2, employeeList.get(1).getId().intValue());
        verify(employeeRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void whenGetEmployeeById_thenMethodFindByIdShouldBeCalled() {
        this.employee = employeeService.getEmployeeById(1L);

        assertEquals(1, employee.getId().intValue());
        assertEquals("Sergey", employee.getFirstName());
        assertEquals("Sergeev", employee.getLastName());
        assertEquals(1, employee.getDepartmentId());
        assertEquals("QA", employee.getJobTitle());
        assertEquals("male", employee.getGender());
        assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employee.getDateOfBirth());
        verify(employeeRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void whenAddEmployee_thenMethodSaveShouldBeCalled() {
        employeeService.addEmployee(employee);

        verify(employeeRepository, Mockito.times(1)).save(employee);
        verify(jmsProducerService, Mockito.times(1)).sendMessage(String.format("Employee %s %s created!", employee.getFirstName(), employee.getLastName()));
    }

    @Test
    public void whenEditEmployeeById_thenMethodFindByIdAndSaveShouldBeCalled() {
        employeeService.editEmployeeById(1L, 2, "Java Developer");

        assertEquals(2, employee.getDepartmentId());
        assertEquals("Java Developer", employee.getJobTitle());
        verify(employeeRepository, Mockito.times(1)).findById(1L);
        verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void whenDeleteEmployeeById_thenMethodDeleteShouldBeCalled() {
        employeeService.deleteEmployeeById(1L);

        verify(employeeRepository, Mockito.times(1)).findById(1L);
        verify(employeeRepository, Mockito.times(1)).delete(employee);
    }
}
