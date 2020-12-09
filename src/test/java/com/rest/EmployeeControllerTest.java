package com.rest;

import com.error.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Employee;
import com.service.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integrations tests of {@link EmployeeController}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        List<Employee> employeeList = Stream.of(Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime())
                .build(), Employee.builder()
                .id(2L)
                .firstName("Natalia")
                .lastName("Sergeeva")
                .departmentId(2)
                .jobTitle("Java Developer")
                .gender("female")
                .dateOfBirth(new GregorianCalendar(1989, Calendar.APRIL, 4).getTime())
                .build())
                .collect(Collectors.toList());

        when(employeeService.getEmployees()).thenReturn(employeeList);

        ResultMatcher status = status().isOk();
        ResultMatcher size = jsonPath("$", Matchers.hasSize(2));
        ResultMatcher firstName1 = jsonPath("$[0].firstName", Matchers.is(employeeList.get(0).getFirstName()));
        ResultMatcher firstName2 = jsonPath("$[1].firstName", Matchers.is(employeeList.get(1).getFirstName()));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andExpect(size)
                .andExpect(firstName1)
                .andExpect(firstName2);
    }

    @Test
    public void givenEmployee_whenGetEmployeeById_thenReturnJson() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new Date())
                .build();

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(employee.getId().intValue())))
                .andExpect(jsonPath("$.firstName", Matchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.departmentId", Matchers.is(1)))
                .andExpect(jsonPath("$.jobTitle", Matchers.is(employee.getJobTitle())))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is(new SimpleDateFormat("yyyy-MM-dd")
                        .format(employee.getDateOfBirth()))));
    }

    @Test
    public void givenConstraintViolationException_whenGetEmployeeById_thenReturnJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("Constraint Violation")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("getEmployeeById.id: must be greater than or equal to 1")));
    }

    @Test
    public void givenResourceNotFoundException_whenGetEmployeeById_thenReturnJson() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenThrow(new ResourceNotFoundException("Employee with ID: 1 Not Found!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Resource Not Found")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("Employee with ID: 1 Not Found!")));
    }

    @Test
    public void whenAddEmployee_thenReturnJson() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new Date())
                .build();

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenMethodArgumentNotValidException_whenAddEmployee_thenReturnJson() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(3)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new Date())
                .build();

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", Matchers.is("Department ID must between 1 and 2")));
    }

    @Test
    public void givenEmployee_whenEditEmployeeById_thenReturnJson() throws Exception {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sergey")
                .lastName("Sergeev")
                .departmentId(1)
                .jobTitle("QA")
                .gender("male")
                .dateOfBirth(new Date())
                .build();

        when(employeeService.editEmployeeById(1L, 1, "QA")).thenReturn(employee);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", employee.getId().toString());
        params.add("departmentId", String.valueOf(employee.getDepartmentId()));
        params.add("jobTitle", employee.getJobTitle());

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(employee.getId().intValue())))
                .andExpect(jsonPath("$.firstName", Matchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.departmentId", Matchers.is(1)))
                .andExpect(jsonPath("$.jobTitle", Matchers.is(employee.getJobTitle())))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is(new SimpleDateFormat("yyyy-MM-dd")
                        .format(employee.getDateOfBirth()))));
    }

    @Test
    public void givenConstraintViolationException_whenEditEmployeeById_thenReturnJson() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(1L));
        params.add("departmentId", String.valueOf(1));
        params.add("jobTitle", "");

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("Constraint Violation")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("editEmployeeById.jobTitle: must be between 3 and 25 characters")));
    }

    @Test
    public void givenResourceNotFoundException_whenEditEmployeeById_thenReturnJson() throws Exception {

        when(employeeService.editEmployeeById(1L, 1, "QA")).thenThrow(new ResourceNotFoundException("Employee with ID: 1 Not Found!"));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(1L));
        params.add("departmentId", String.valueOf(1));
        params.add("jobTitle", "QA");

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Resource Not Found")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("Employee with ID: 1 Not Found!")));

    }

    @Test
    public void whenDeleteEmployeeById_thenReturnJson() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(1L));

        mockMvc.perform(delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenConstraintViolationException_whenDeleteEmployeeByIdTest_thenReturnJson() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(0L));

        mockMvc.perform(delete("/employees/0")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("Constraint Violation")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("deleteEmployeeById.id: must be greater than or equal to 1")));
    }

    @Test
    public void givenResourceNotFoundException_whenDeleteEmployeeByIdTest_thenReturnJson() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(1L));

        doThrow(new ResourceNotFoundException("Employee with ID: 1 Not Found!")).when(employeeService).deleteEmployeeById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Resource Not Found")))
                .andExpect(jsonPath("$.errors[0]", Matchers.is("Employee with ID: 1 Not Found!")));
    }
}
