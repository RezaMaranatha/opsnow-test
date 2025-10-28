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
public class EmployeeCumulativeSalary {
    private String departmentCode;
    private String employeeNumber;
    private String employeeName;
    private BigDecimal cumulativeSalary;
}
