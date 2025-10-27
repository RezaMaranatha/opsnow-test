package com.ram.opsnow.employee.controller;

import com.ram.opsnow.employee.dto.EmployeeRequestDTO;
import com.ram.opsnow.employee.dto.EmployeeResponseDTO;
import com.ram.opsnow.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create employee", description = "Create a new employee")
    public ResponseEntity<EmployeeRequestDTO> createEmployee(@RequestBody EmployeeRequestDTO employeeDTO) {
        EmployeeRequestDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(createdEmployee);
    }

    @GetMapping("/{employeeNumber}")
    @Operation(summary = "Get employee by employee number", description = "Get employee details by employee number")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmployeeNumber(@PathVariable String employeeNumber) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmployeeNumber(employeeNumber);
        return ResponseEntity.ok(employee);
    }
}