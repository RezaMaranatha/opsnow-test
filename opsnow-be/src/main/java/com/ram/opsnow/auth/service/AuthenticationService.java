package com.ram.opsnow.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ram.opsnow.auth.dto.AuthenticationRequest;
import com.ram.opsnow.auth.dto.AuthenticationResponse;
import com.ram.opsnow.auth.util.JwtUtil;
import com.ram.opsnow.employee.entity.Employee;
import com.ram.opsnow.employee.repository.EmployeeRepository;
import com.ram.opsnow.exception.DataNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    public AuthenticationService(AuthenticationManager authenticationManager,
            EmployeeUserDetailsService userDetailsService, JwtUtil jwtUtil, EmployeeRepository employeeRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Employee employee = employeeRepository.findByEmail(request.getEmail())
                    .orElseThrow(
                            () -> new DataNotFoundException("Employee not found with email: " + request.getEmail()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return AuthenticationResponse.builder()
                    .token(token)
                    .employeeNumber(employee.getEmployeeNumber())
                    .employeeName(employee.getEmployeeName())
                    .email(employee.getEmail())
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            log.error("Authentication failed for email: {} - Error: {}", request.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
