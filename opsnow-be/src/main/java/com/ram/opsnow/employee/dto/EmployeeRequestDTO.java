package com.ram.opsnow.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {
    private String employeeNumber;
    private String employeeName;
    private String tierCode;
    private String locationCode;
    private String departmentCode;
    private String supervisorCode;
    private BigDecimal salary;
    private LocalDateTime entryDate;
    private String email;
    private String password;
}
