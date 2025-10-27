package com.ram.opsnow.employee.service;

import com.ram.opsnow.employee.dto.EmployeeRequestDTO;
import com.ram.opsnow.employee.dto.EmployeeResponseDTO;
import com.ram.opsnow.employee.entity.Employee;
import com.ram.opsnow.employee.repository.EmployeeRepository;
import com.ram.opsnow.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public EmployeeRequestDTO createEmployee(EmployeeRequestDTO employeeDTO) {
        employeeDTO.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);

        return modelMapper.map(savedEmployee, EmployeeRequestDTO.class);
    }

    public EmployeeResponseDTO getEmployeeByEmployeeNumber(String employeeNumber) {
        Employee employee = employeeRepository.findById(employeeNumber)
                .orElseThrow(
                        () -> new DataNotFoundException("Employee not found with employee number: " + employeeNumber));

        return modelMapper.map(employee, EmployeeResponseDTO.class);
    }
}
