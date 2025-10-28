package com.ram.opsnow.employee.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDepartmentAnalysis {
    private String locationName;
    private String deptWithMostEmployee;
    private String deptEmployeeCount;
    private BigDecimal lowestDeptAvgSalary;
}
