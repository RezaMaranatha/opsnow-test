package com.ram.opsnow.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ram.opsnow.department.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {

}
