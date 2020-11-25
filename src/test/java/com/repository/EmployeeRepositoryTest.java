package com.repository;

import com.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integrations tests of {@link EmployeeRepository}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Rollback(value = true)
    public void whenFindAll_thenReturnEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAll();

        assertEquals(2, employeeList.size());

        assertEquals(1, employeeList.get(0).getId().intValue());
        assertEquals("Sergey", employeeList.get(0).getFirstName());
        assertEquals("Sergeev", employeeList.get(0).getLastName());
        assertEquals(1, employeeList.get(0).getDepartmentId());
        assertEquals("Java Developer", employeeList.get(0).getJobTitle());
        assertEquals("male", employeeList.get(0).getGender());
        assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employeeList.get(0).getDateOfBirth());

        assertEquals(2, employeeList.get(1).getId().intValue());
        assertEquals("Natasha", employeeList.get(1).getFirstName());
        assertEquals("Sergeeva", employeeList.get(1).getLastName());
        assertEquals(2, employeeList.get(1).getDepartmentId());
        assertEquals("QA", employeeList.get(1).getJobTitle());
        assertEquals("female", employeeList.get(1).getGender());
        assertEquals(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime(), employeeList.get(1).getDateOfBirth());

    }

    @Test
    @Rollback(value = true)
    public void whenFindById_thenReturnEmployee() {
        Employee employee = employeeRepository.findById(1L).get();

        assertEquals(1, employee.getId().intValue());
        assertEquals("Sergey", employee.getFirstName());
        assertEquals("Sergeev", employee.getLastName());
        assertEquals(1, employee.getDepartmentId());
        assertEquals("Java Developer", employee.getJobTitle());
        assertEquals("male", employee.getGender());
        assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employee.getDateOfBirth());
    }

    @Test
    @Rollback(value = true)
    public void whenSave_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("Petya")
                .lastName("Petrov")
                .departmentId(1)
                .jobTitle("JS Developer")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1995, Calendar.MARCH, 15).getTime())
                .build();

        //when
        employeeRepository.save(employee);
        employee = entityManager.find(Employee.class, 3L);

        //then
        assertEquals(3, employee.getId().intValue());
        assertEquals("Petya", employee.getFirstName());
        assertEquals("Petrov", employee.getLastName());
        assertEquals(1, employee.getDepartmentId());
        assertEquals("JS Developer", employee.getJobTitle());
        assertEquals("male", employee.getGender());
        assertEquals(new GregorianCalendar(1995, Calendar.MARCH, 15).getTime(), employee.getDateOfBirth());
    }

    @Test
    public void deleteById() {
        employeeRepository.deleteById(1L);
        List<Employee> employeeList = employeeRepository.findAll();

        assertEquals(1 ,employeeList.size());

        assertEquals(2, employeeList.get(0).getId().intValue());
        assertEquals("Natasha", employeeList.get(0).getFirstName());
        assertEquals("Sergeeva", employeeList.get(0).getLastName());
        assertEquals(2, employeeList.get(0).getDepartmentId());
        assertEquals("QA", employeeList.get(0).getJobTitle());
        assertEquals("female", employeeList.get(0).getGender());
        assertEquals(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime(), employeeList.get(0).getDateOfBirth());
    }
}
