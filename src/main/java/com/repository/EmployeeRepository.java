package com.repository;

import com.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA repository for {@link com.model.Employee}
 * Implementation of {@link JpaRepository} interface.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
