package com.repository;

import com.model.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Integrations tests of {@link EmployeeRepository}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Rollback(value = true)
    public void whenFindAll_thenReturnEmployeeList() {
        List<Employee> employeeList = Stream.of(Employee.builder()
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime())
                .build(), Employee.builder()
                .firstName("Natalia")
                .lastName("Sergeeva")
                .departmentId(2)
                .jobTitle("Java Developer")
                .gender("female")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime())
                .build())
                .collect(Collectors.toList());

        for (Employee employee : employeeList) {
            entityManager.persist(employee);
        }
        entityManager.flush();

        employeeList = employeeRepository.findAll();


        Assert.assertEquals(2, employeeList.size());

        Assert.assertEquals(1, employeeList.get(0).getId().intValue());
        Assert.assertEquals("Sergey", employeeList.get(0).getFirstName());
        Assert.assertEquals("Sergeev", employeeList.get(0).getLastName());
        Assert.assertEquals(1, employeeList.get(0).getDepartmentId());
        Assert.assertEquals("QA", employeeList.get(0).getJobTitle());
        Assert.assertEquals("male", employeeList.get(0).getGender());
        Assert.assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employeeList.get(0).getDateOfBirth());

        Assert.assertEquals(2, employeeList.get(1).getId().intValue());
        Assert.assertEquals("Natalia", employeeList.get(1).getFirstName());
        Assert.assertEquals("Sergeeva", employeeList.get(1).getLastName());
        Assert.assertEquals(2, employeeList.get(1).getDepartmentId());
        Assert.assertEquals("Java Developer", employeeList.get(1).getJobTitle());
        Assert.assertEquals("female", employeeList.get(1).getGender());
        Assert.assertEquals(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime(), employeeList.get(1).getDateOfBirth());
    }

    @Test
    @Rollback(value = true)
    public void whenFindById_thenReturnEmployee() {
        Employee employee = Employee.builder()
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime())
                .build();
        entityManager.persist(employee);
        entityManager.flush();

        employee = employeeRepository.findById(1L).get();

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals("Sergey", employee.getFirstName());
        Assert.assertEquals("Sergeev", employee.getLastName());
        Assert.assertEquals(1, employee.getDepartmentId());
        Assert.assertEquals("QA", employee.getJobTitle());
        Assert.assertEquals("male", employee.getGender());
        Assert.assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employee.getDateOfBirth());
    }

    @Test
    @Rollback(value = true)
    public void whenSave_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("Petya")
                .lastName("Petrov")
                .departmentId(2)
                .jobTitle("JS Developer")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1995, Calendar.MARCH, 15).getTime())
                .build();

        //when
        employeeRepository.save(employee);
        employee = entityManager.find(Employee.class, 1L);

        //then
        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals("Petya", employee.getFirstName());
        Assert.assertEquals("Petrov", employee.getLastName());
        Assert.assertEquals(2, employee.getDepartmentId());
        Assert.assertEquals("JS Developer", employee.getJobTitle());
        Assert.assertEquals("male", employee.getGender());
        Assert.assertEquals(new GregorianCalendar(1995, Calendar.MARCH, 15).getTime(), employee.getDateOfBirth());
    }
}
