package com.ram.opsnow.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CsvImportService {
	private final JdbcTemplate jdbcTemplate;

	public CsvImportService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public void importEmployee(String filePath) throws IOException {
		// ✅ 1. Create table if not exists
		createEmployeeTable();

		// ✅ 2. Insert CSV data
		String sql = "INSERT INTO employee (employee_number,employee_name,tier_code,location_code,department_code,supervisor_code,salary,entry_date,email,password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			List<Object[]> batchArgs = new ArrayList<>();
			boolean skipHeader = true;

			while ((line = br.readLine()) != null) {
				if (skipHeader) { // skip header row
					skipHeader = false;
					continue;
				}

				String[] data = line.split(";", -1); // 👈 -1 keeps empty strings instead of trimming
				if (data.length < 10)
					continue;

				batchArgs.add(new Object[] {
						emptyToNull(data[0]),
						emptyToNull(data[1]),
						emptyToNull(data[2]),
						emptyToNull(data[3]),
						emptyToNull(data[4]),
						emptyToNull(data[5]),
						parseBigDecimalOrNull(data[6]),
						parseTimestampOrNull(data[7]),
						emptyToNull(data[8]),
						emptyToNull(data[9])
				});
			}

			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}

	private void createEmployeeTable() {
		String createTableSQL = """
					CREATE TABLE employee (
						employee_number varchar(50) PRIMARY KEY NOT NULL,
						employee_name varchar(200) NOT NULL,
						tier_code varchar(5) not null,
						location_code varchar(5) not null,
						department_code varchar(5) not null,
						supervisor_code varchar(50),
						salary NUMERIC(19, 4),
						entry_date TIMESTAMP,
						email varchar NOT NULL,
						password varchar not null,
						FOREIGN KEY (tier_code) REFERENCES tier(tier_code),
						FOREIGN KEY (location_code) REFERENCES location(location_code),
						FOREIGN KEY (department_code) REFERENCES department(department_code)
					);
				""";
		jdbcTemplate.execute("DROP TABLE IF EXISTS employee");
		jdbcTemplate.execute(createTableSQL);
	}

	@Transactional
	public void importLocation(String filePath) throws IOException {
		// ✅ 1. Create table if not exists
		createLocationTable();

		// ✅ 2. Insert CSV data
		String sql = "INSERT INTO location (location_code, location_name,location_address) VALUES (?, ?, ?)";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			List<Object[]> batchArgs = new ArrayList<>();
			boolean skipHeader = true;

			while ((line = br.readLine()) != null) {
				if (skipHeader) { // skip header row
					skipHeader = false;
					continue;
				}

				String[] data = line.split(";", -1); // 👈 -1 keeps empty strings instead of trimming
				if (data.length < 3)
					continue;

				batchArgs.add(new Object[] {
						emptyToNull(data[0]),
						emptyToNull(data[1]),
						emptyToNull(data[2])
				});
			}

			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}

	private void createLocationTable() {
		String createTableSQL = """
						CREATE TABLE location (
						    location_code varchar(5) PRIMARY KEY NOT NULL,
						    location_name varchar(200) NOT NULL,
						    location_address varchar(300) NOT NULL
						);
				""";
		jdbcTemplate.execute("DROP TABLE IF EXISTS location");
		jdbcTemplate.execute(createTableSQL);
	}

	@Transactional
	public void importTier(String filePath) throws IOException {
		// ✅ 1. Create table if not exists
		createTierTable();

		// ✅ 2. Insert CSV data
		String sql = "INSERT INTO tier (tier_code, tier_name) VALUES (?, ?)";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			List<Object[]> batchArgs = new ArrayList<>();
			boolean skipHeader = true;

			while ((line = br.readLine()) != null) {
				if (skipHeader) { // skip header row
					skipHeader = false;
					continue;
				}

				String[] data = line.split(";", -1); // 👈 -1 keeps empty strings instead of trimming
				if (data.length < 2)
					continue;

				batchArgs.add(new Object[] {
						emptyToNull(data[0]),
						emptyToNull(data[1])
				});
			}

			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}

	private void createTierTable() {
		String createTableSQL = """
						CREATE TABLE tier (
							tier_code varchar(5) PRIMARY KEY NOT NULL,
							tier_name varchar(200) NOT NULL
						);
				""";
		jdbcTemplate.execute("DROP TABLE IF EXISTS tier");
		jdbcTemplate.execute(createTableSQL);
	}

	@Transactional
	public void importDepartment(String filePath) throws IOException {
		// ✅ 1. Create table if not exists
		createDepartmentTable();

		// ✅ 2. Insert CSV data
		String sql = "INSERT INTO department (department_code, department_name) VALUES (?, ?)";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			List<Object[]> batchArgs = new ArrayList<>();
			boolean skipHeader = true;

			while ((line = br.readLine()) != null) {
				if (skipHeader) { // skip header row
					skipHeader = false;
					continue;
				}

				String[] data = line.split(";", -1); // 👈 -1 keeps empty strings instead of trimming
				if (data.length < 2)
					continue;

				batchArgs.add(new Object[] {
						emptyToNull(data[0]),
						emptyToNull(data[1])
				});
			}

			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}

	private void createDepartmentTable() {
		String createTableSQL = """
						CREATE TABLE department (
							department_code varchar(5) PRIMARY KEY NOT NULL,
							department_name varchar(200) NOT NULL
						);
				""";
		jdbcTemplate.execute("DROP TABLE IF EXISTS department");
		jdbcTemplate.execute(createTableSQL);
	}

	private String emptyToNull(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value.trim();
	}

	private BigDecimal parseBigDecimalOrNull(String value) {
		try {
			return (value == null || value.trim().isEmpty()) ? null : new BigDecimal(value.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private LocalDateTime parseTimestampOrNull(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		String v = value.trim();
		List<DateTimeFormatter> formatters = List.of(
				DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

		for (DateTimeFormatter formatter : formatters) {
			try {
				return LocalDateTime.parse(v, formatter);
			} catch (DateTimeParseException e) {
				log.error(e.getMessage());
			}
		}

		// Optional: log unparseable timestamps
		log.error("⚠️ Unrecognized timestamp format: " + v);
		return null;
	}
}
