package com.ram.opsnow.util;

import org.springframework.data.jpa.domain.Specification;

import com.ram.opsnow.employee.entity.Employee;

public class EmployeeSpecification {
    private EmployeeSpecification() {
    }

    public static Specification<Employee> hasEmployeeNumber(String employeeNumber) {
        return (root, query, criteriaBuilder) -> employeeNumber == null ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeNumber")),
                        employeeNumber.toLowerCase());
    }

    public static Specification<Employee> hasEmployeeName(String employeeName) {
        return (root, query, criteriaBuilder) -> employeeName == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeName")),
                        "%" + employeeName.toLowerCase() + "%");
    }

    public static Specification<Employee> hasEmployeeEmail(String employeeEmail) {
        return (root, query, criteriaBuilder) -> employeeEmail == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeEmail")),
                        "%" + employeeEmail.toLowerCase() + "%");
    }
}
