package com.ram.opsnow.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
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
