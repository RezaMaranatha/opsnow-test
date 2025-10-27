package com.ram.opsnow.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ram.opsnow.employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
