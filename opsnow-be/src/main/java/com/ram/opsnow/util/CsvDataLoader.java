package com.ram.opsnow.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CsvDataLoader implements CommandLineRunner {
	private final CsvImportService csvImportService;

	public CsvDataLoader(CsvImportService csvImportService) {
		this.csvImportService = csvImportService;
	}

	@Override
	public void run(String... args) throws Exception {
		csvImportService.deleteEmployeeTable();
		csvImportService.importDepartment("src/main/resources/data/department.csv");
		log.info("Department data imported");
		csvImportService.importTier("src/main/resources/data/tier.csv");
		log.info("Tier data imported");
		csvImportService.importLocation("src/main/resources/data/location.csv");
		log.info("Location data imported");
		csvImportService.importEmployee("src/main/resources/data/employee.csv");
		log.info("Employee data imported");
		csvImportService.createLoggingTable();
		log.info("Logging table created");
	}
}
