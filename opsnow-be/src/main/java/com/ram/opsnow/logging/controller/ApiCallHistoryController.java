package com.ram.opsnow.logging.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.opsnow.logging.dto.ApiCallHistoryDTO;
import com.ram.opsnow.logging.entity.ApiCallHistory;
import com.ram.opsnow.logging.service.ApiCallHistoryService;
import com.ram.opsnow.util.PaginationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logging")
@RequiredArgsConstructor
@Tag(name = "API Logging", description = "API call history management APIs")
public class ApiCallHistoryController {

    private final ApiCallHistoryService apiCallHistoryService;

    @GetMapping("/history")
    @Operation(summary = "Get API call history", description = "Retrieve paginated API call history")
    public ResponseEntity<PaginationResponse<ApiCallHistoryDTO>> getApiCallHistory(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "timestamp") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(apiCallHistoryService.getApiCallHistory(pageable));
    }

    @GetMapping("/history/user/{userIdentifier}")
    @Operation(summary = "Get API call history by user", description = "Retrieve API call history for a specific user")
    public ResponseEntity<PaginationResponse<ApiCallHistoryDTO>> getApiCallHistoryByUser(
            @Parameter(description = "User identifier") @PathVariable String userIdentifier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(apiCallHistoryService.getApiCallHistoryByUser(userIdentifier, pageable));
    }

    @GetMapping("/history/endpoint")
    @Operation(summary = "Get API call history by endpoint", description = "Retrieve API call history for a specific endpoint")
    public ResponseEntity<PaginationResponse<ApiCallHistoryDTO>> getApiCallHistoryByEndpoint(
            @Parameter(description = "Endpoint pattern") @RequestParam String endpoint,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(apiCallHistoryService.getApiCallHistoryByEndpoint(endpoint, pageable));
    }

    @GetMapping("/history/method/{httpMethod}")
    @Operation(summary = "Get API call history by HTTP method", description = "Retrieve API call history for a specific HTTP method")
    public ResponseEntity<PaginationResponse<ApiCallHistoryDTO>> getApiCallHistoryByMethod(
            @Parameter(description = "HTTP method") @PathVariable String httpMethod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(apiCallHistoryService.getApiCallHistoryByMethod(httpMethod, pageable));
    }

    @GetMapping("/history/date-range")
    @Operation(summary = "Get API call history by date range", description = "Retrieve API call history within a specific date range")
    public ResponseEntity<PaginationResponse<ApiCallHistoryDTO>> getApiCallHistoryByDateRange(
            @Parameter(description = "Start date (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(apiCallHistoryService.getApiCallHistoryByDateRange(startDate, endDate, pageable));
    }
}
