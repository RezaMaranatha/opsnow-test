package com.ram.opsnow.employee.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ram.opsnow.employee.dto.EmployeeCumulativeSalary;
import com.ram.opsnow.employee.dto.EmployeeDepartmentAnalysis;
import com.ram.opsnow.employee.dto.EmployeeRanking;
import com.ram.opsnow.employee.dto.EmployeeRequestDTO;
import com.ram.opsnow.employee.dto.EmployeeResponseDTO;
import com.ram.opsnow.employee.entity.Employee;
import com.ram.opsnow.employee.repository.EmployeeRepository;
import com.ram.opsnow.exception.DataNotFoundException;
import com.ram.opsnow.util.EmployeeSpecification;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final JdbcTemplate jdbcTemplate;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public PaginationResponse<EmployeeResponseDTO> getAllEmployee(int pageNumber, int pageSize, String sortBy,
			String employeeNumber,
			String employeeName, String employeeEmail) {
		Sort orderBy = Sort.by(Sort.Order.asc(sortBy));
		Pageable pageable = PageRequest.of(pageNumber, pageSize, orderBy);

		Specification<Employee> spec = Specification.allOf(EmployeeSpecification.hasEmployeeNumber(employeeNumber),
				EmployeeSpecification.hasEmployeeName(employeeName),
				EmployeeSpecification.hasEmployeeEmail(employeeEmail));

		var employees = employeeRepository.findAll(spec, pageable);
		List<EmployeeResponseDTO> employeeDtos = employees.getContent().stream()
				.map(tier -> modelMapper.map(tier, EmployeeResponseDTO.class))
				.toList();

		return new PaginationResponse<>(
				employeeDtos,
				employees.getNumber(),
				employees.getSize(),
				employees.getTotalElements(),
				employees.getTotalPages());
	}

	public EmployeeResponseDTO getEmployeeByEmployeeNumber(String employeeNumber) {
		Employee employee = employeeRepository.findById(employeeNumber)
				.orElseThrow(
						() -> new DataNotFoundException("Employee not found with employee number: " + employeeNumber));

		return modelMapper.map(employee, EmployeeResponseDTO.class);
	}

	public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeDTO) {
		employeeDTO.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));

		Employee employee = modelMapper.map(employeeDTO, Employee.class);
		Employee savedEmployee = employeeRepository.save(employee);

		return modelMapper.map(savedEmployee, EmployeeResponseDTO.class);
	}

	@Override
	public EmployeeResponseDTO updateEmployee(EmployeeRequestDTO employeeDTO) {
		if (!employeeRepository.existsById(employeeDTO.getEmployeeNumber())) {
			throw new DataNotFoundException("Location not found with code: " + employeeDTO.getLocationCode());
		}
		var employee = employeeRepository.save(modelMapper.map(employeeDTO, Employee.class));
		return modelMapper.map(employee, EmployeeResponseDTO.class);
	}

	@Override
	public String deleteEmployee(String employeeNumber) {
		try {
			employeeRepository.deleteById(employeeNumber);
		} catch (Exception e) {
			return "Failed to delete location with code: " + employeeNumber;
		}
		return "Location with code " + employeeNumber + " deleted successfully.";
	}

	@Override
	public List<EmployeeCumulativeSalary> getEmployeeCumulativeSalary() {
		try {
			String sql = """
						SELECT
							e1.department_code,
							e1.employee_number,
							e1.employee_name,
							e1.salary,
							(
								SELECT SUM(e2.salary)
								FROM employee e2
								WHERE e2.department_code = e1.department_code
									AND e2.employee_number <= e1.employee_number
							) AS cumulative_salary
						FROM employee e1
						ORDER BY e1.department_code, e1.employee_number;
					""";

			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				EmployeeCumulativeSalary ecs = new EmployeeCumulativeSalary();
				ecs.setDepartmentCode(rs.getString("department_code"));
				ecs.setEmployeeNumber(rs.getString("employee_number"));
				ecs.setEmployeeName(rs.getString("employee_name"));
				ecs.setCumulativeSalary(rs.getBigDecimal("cumulative_salary"));
				return ecs;
			});

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("Failed to retrieve employee cumulative salaries", e);
		}
	}

	@Override
	public List<EmployeeDepartmentAnalysis> getEmployeeDepartmentAnalysis() {
		String sql = """
								WITH dept_count_avgsalary AS (
				    SELECT
				        l.location_name,
				        d.department_name,
				        COUNT(*) AS dept_employee_count,
				        AVG(e.salary) AS avg_salary
				    FROM employee e
				    join location l
				    on e.location_code = l.location_code
				    join department d
				    on e.department_code = d.department_code
				    GROUP BY location_name, department_name
				),
				highest_employee_count AS (
				    SELECT dc1.*
				    FROM dept_count_avgsalary dc1
				    WHERE dc1.dept_employee_count = (
				        SELECT MAX(dc2.dept_employee_count)
				        FROM dept_count_avgsalary dc2
				        WHERE dc2.location_name = dc1.location_name
				    )
				),
				lowest_avg_salary AS (
				    SELECT dc1.*
				    FROM dept_count_avgsalary dc1
				    WHERE dc1.avg_salary = (
				        SELECT MIN(dc2.avg_salary)
				        FROM dept_count_avgsalary dc2
				        WHERE dc2.location_name = dc1.location_name
				    )
				)
				SELECT
				    h.location_name,
				    h.department_name AS dept_with_most_employee,
				    h.dept_employee_count,
				    l.avg_salary AS lowest_dept_avg_salary
				FROM highest_employee_count h
				JOIN lowest_avg_salary l
				    ON h.location_name = l.location_name
				ORDER BY h.location_name;
								""";
		try {
			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				EmployeeDepartmentAnalysis eda = new EmployeeDepartmentAnalysis();
				eda.setLocationName(rs.getString("location_name"));
				eda.setDeptWithMostEmployee(rs.getString("dept_with_most_employee"));
				eda.setDeptEmployeeCount(rs.getString("dept_employee_count"));
				eda.setLowestDeptAvgSalary(rs.getBigDecimal("lowest_dept_avg_salary"));
				return eda;
			});
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("Failed to retrieve employee department analysis", e);
		}
	}

	@Override
	public List<EmployeeRanking> getEmployeeRanking() {
		String sql = """
				WITH ranked_employees AS (
				    SELECT
				        e.employee_number,
				        e.employee_name,
				        t.tier_name,
				        d.department_name,
				        l.location_name,
				        e.salary,
				        (
				            SELECT COUNT(DISTINCT e2.salary) + 1
				            FROM employee e2
				            WHERE e2.location_code = e.location_code
				              AND e2.department_code = e.department_code
				              AND e2.salary > e.salary
				        ) AS rank
				    FROM employee e
				    JOIN department d ON e.department_code = d.department_code
				    JOIN location l ON e.location_code = l.location_code
				    JOIN tier t ON e.tier_code = t.tier_code
				),
				previous_ranks AS (
				    SELECT
				        location_name,
				        department_name,
				        rank,
				        salary
				    FROM ranked_employees
				)
				SELECT
				    a.location_name,
				    a.department_name,
				    a.employee_name,
				    a.tier_name as position,
				    a.salary,
				    a.rank,
				    CASE
				        WHEN b.salary IS NULL THEN 0
				        WHEN b.salary = a.salary THEN 0
				        ELSE b.salary - a.salary
				    END AS salary_gap
				FROM ranked_employees a
				LEFT JOIN previous_ranks b
				    ON a.location_name = b.location_name
				    AND a.department_name = b.department_name
				    AND b.rank = a.rank - 1
				ORDER BY a.location_name, a.department_name, a.rank
				""";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new EmployeeRanking(
				rs.getString("location_name"),
				rs.getString("department_name"),
				rs.getString("employee_name"),
				rs.getString("position"),
				rs.getBigDecimal("salary"),
				rs.getString("rank"),
				rs.getBigDecimal("salary_gap")));
	}
}