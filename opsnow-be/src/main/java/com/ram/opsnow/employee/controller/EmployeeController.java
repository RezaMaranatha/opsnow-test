package com.ram.opsnow.employee.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.opsnow.employee.dto.EmployeeCumulativeSalary;
import com.ram.opsnow.employee.dto.EmployeeDepartmentAnalysis;
import com.ram.opsnow.employee.dto.EmployeeRanking;
import com.ram.opsnow.employee.dto.EmployeeRequestDTO;
import com.ram.opsnow.employee.dto.EmployeeResponseDTO;
import com.ram.opsnow.employee.service.EmployeeServiceImpl;
import com.ram.opsnow.util.PaginationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @GetMapping
    public ResponseEntity<PaginationResponse<EmployeeResponseDTO>> getAllEmployee(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "employeeName") String sortBy,
            @RequestParam(required = false) String employeeNumber,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String employeeEmail) {
        return ResponseEntity
                .ok(employeeService.getAllEmployee(pageNumber, pageSize, sortBy, employeeNumber, employeeName,
                        employeeEmail));
    }

    @GetMapping("/{employeeNumber}")
    @Operation(summary = "Get employee by employee number", description = "Get employee details by employee number")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmployeeNumber(@PathVariable String employeeNumber) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmployeeNumber(employeeNumber);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Create a new employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody EmployeeRequestDTO employeeDTO) {
        EmployeeResponseDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping
    public ResponseEntity<EmployeeResponseDTO> updateTier(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeRequestDTO));
    }

    @DeleteMapping(value = "/{employeeNumber}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeNumber) {
        return ResponseEntity.ok(employeeService.deleteEmployee(employeeNumber));
    }

    @GetMapping("/cumulative-salary")
    @Operation(summary = "Get employee cumulative salary by department", description = "Get employee cumulative salary by department")
    public ResponseEntity<List<EmployeeCumulativeSalary>> getEmployeeCumulativeSalary() {
        List<EmployeeCumulativeSalary> cumulativeSalary = employeeService.getEmployeeCumulativeSalary();
        return ResponseEntity.ok(cumulativeSalary);
    }

    @GetMapping("/department-analysis")
    @Operation(summary = "Get employee department analysis", description = "Get employee department analysis")
    public ResponseEntity<List<EmployeeDepartmentAnalysis>> getEmployeeDepartmentAnalyis() {
        List<EmployeeDepartmentAnalysis> cumulativeSalary = employeeService.getEmployeeDepartmentAnalysis();
        return ResponseEntity.ok(cumulativeSalary);
    }

    @GetMapping("/employee-ranking")
    @Operation(summary = "Get employee ranking", description = "Get employee ranking")
    public ResponseEntity<List<EmployeeRanking>> getEmployeeRanking() {
        List<EmployeeRanking> cumulativeSalary = employeeService.getEmployeeRanking();
        return ResponseEntity.ok(cumulativeSalary);
    }
}