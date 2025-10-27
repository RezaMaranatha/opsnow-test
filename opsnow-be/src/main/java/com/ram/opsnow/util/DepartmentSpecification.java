package com.ram.opsnow.util;

import org.springframework.data.jpa.domain.Specification;

import com.ram.opsnow.department.entity.Department;

public class DepartmentSpecification {
    private DepartmentSpecification() {
    }

    public static Specification<Department> hasDepartmentCode(String departmentCode) {
        return (root, query, criteriaBuilder) -> departmentCode == null ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("departmentCode")),
                        departmentCode.toLowerCase());
    }

    public static Specification<Department> hasDepartmentName(String departmentName) {
        return (root, query, criteriaBuilder) -> departmentName == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("departmentName")),
                        "%" + departmentName.toLowerCase() + "%");
    }
}
