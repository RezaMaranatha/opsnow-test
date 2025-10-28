package com.ram.opsnow.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ram.opsnow.employee.entity.Employee;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByEmail(String email);

    Employee findFirstByOrderByEmployeeNumberDesc();
}