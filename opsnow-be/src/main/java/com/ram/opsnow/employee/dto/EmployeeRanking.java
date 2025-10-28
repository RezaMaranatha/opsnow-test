package com.ram.opsnow.employee.dto;

import java.math.BigDecimal;

public record EmployeeRanking(
		String locationName,
		String departmentName,
		String employeeName,
		String position,
		BigDecimal salary,
		String rank,
		BigDecimal salaryGap) {
}
