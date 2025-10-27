package com.ram.opsnow.logging.service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ram.opsnow.logging.entity.ApiCallHistory;
import com.ram.opsnow.logging.repository.ApiCallHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCallHistoryService {

    private final ApiCallHistoryRepository apiCallHistoryRepository;

    @Async("apiLoggingExecutor")
    public CompletableFuture<Void> logApiCall(ApiCallHistory apiCallHistory) {
        try {
            apiCallHistoryRepository.save(apiCallHistory);
            log.debug("API call logged successfully for endpoint: {}", apiCallHistory.getApiEndpoint());
        } catch (Exception e) {
            log.error("Failed to log API call for endpoint: {} - Error: {}",
                    apiCallHistory.getApiEndpoint(), e.getMessage(), e);
        }
        return CompletableFuture.completedFuture(null);
    }

    public Page<ApiCallHistory> getApiCallHistory(Pageable pageable) {
        return apiCallHistoryRepository.findAll(pageable);
    }

    public Page<ApiCallHistory> getApiCallHistoryByUser(String userIdentifier, Pageable pageable) {
        return apiCallHistoryRepository.findByUserIdentifierOrderByTimestampDesc(userIdentifier, pageable);
    }

    public Page<ApiCallHistory> getApiCallHistoryByEndpoint(String endpoint, Pageable pageable) {
        return apiCallHistoryRepository.findByApiEndpointContainingIgnoreCaseOrderByTimestampDesc(endpoint, pageable);
    }

    public Page<ApiCallHistory> getApiCallHistoryByMethod(String httpMethod, Pageable pageable) {
        return apiCallHistoryRepository.findByHttpMethodOrderByTimestampDesc(httpMethod, pageable);
    }

    public Page<ApiCallHistory> getApiCallHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate,
            Pageable pageable) {
        return apiCallHistoryRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate, pageable);
    }
}
