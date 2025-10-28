package com.ram.opsnow.employee.service;

import java.util.List;

import com.ram.opsnow.employee.dto.EmployeeCumulativeSalary;
import com.ram.opsnow.employee.dto.EmployeeDepartmentAnalysis;
import com.ram.opsnow.employee.dto.EmployeeRanking;
import com.ram.opsnow.employee.dto.EmployeeRequestDTO;
import com.ram.opsnow.employee.dto.EmployeeResponseDTO;
import com.ram.opsnow.util.PaginationResponse;

public interface EmployeeService {
	PaginationResponse<EmployeeResponseDTO> getAllEmployee(int pageNumber, int pageSize, String sortBy,
			String employeeNumber,
			String employeeName, String employeeEmail);

	EmployeeResponseDTO getEmployeeByEmployeeNumber(String employeeNumber);

	EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeDTO);

	EmployeeResponseDTO updateEmployee(EmployeeRequestDTO employeeDTO);

	String deleteEmployee(String employeeNumber);

	List<EmployeeCumulativeSalary> getEmployeeCumulativeSalary();

	List<EmployeeDepartmentAnalysis> getEmployeeDepartmentAnalysis();

	List<EmployeeRanking> getEmployeeRanking();
}
