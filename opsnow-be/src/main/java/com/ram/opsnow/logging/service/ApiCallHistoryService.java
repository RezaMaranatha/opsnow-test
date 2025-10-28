package com.ram.opsnow.logging.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ram.opsnow.logging.dto.ApiCallHistoryDTO;
import com.ram.opsnow.logging.entity.ApiCallHistory;
import com.ram.opsnow.logging.repository.ApiCallHistoryRepository;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiCallHistoryService {

	private final ApiCallHistoryRepository apiCallHistoryRepository;
	private final ModelMapper modelMapper;

	public ApiCallHistoryService(ApiCallHistoryRepository apiCallHistoryRepository, ModelMapper modelMapper) {
		this.apiCallHistoryRepository = apiCallHistoryRepository;
		this.modelMapper = modelMapper;
	}

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

	public PaginationResponse<ApiCallHistoryDTO> getApiCallHistory(Pageable pageable) {
		var history = apiCallHistoryRepository.findAll(pageable);
		List<ApiCallHistoryDTO> apiCallHistories = history.getContent().stream()
				.map(apiCallHistory -> modelMapper.map(apiCallHistory, ApiCallHistoryDTO.class))
				.toList();
		return new PaginationResponse<>(
				apiCallHistories,
				history.getNumber(),
				history.getSize(),
				history.getTotalElements(),
				history.getTotalPages());
	}

	public PaginationResponse<ApiCallHistoryDTO> getApiCallHistoryByUser(String userIdentifier, Pageable pageable) {
		var history = apiCallHistoryRepository.findByUserIdentifierOrderByTimestampDesc(userIdentifier, pageable);
		List<ApiCallHistoryDTO> apiCallHistories = history.getContent().stream()
				.map(apiCallHistory -> modelMapper.map(apiCallHistory, ApiCallHistoryDTO.class))
				.toList();
		return new PaginationResponse<>(
				apiCallHistories,
				history.getNumber(),
				history.getSize(),
				history.getTotalElements(),
				history.getTotalPages());
	}

	public PaginationResponse<ApiCallHistoryDTO> getApiCallHistoryByEndpoint(String endpoint, Pageable pageable) {
		var history = apiCallHistoryRepository.findByApiEndpointContainingIgnoreCaseOrderByTimestampDesc(endpoint,
				pageable);
		List<ApiCallHistoryDTO> apiCallHistories = history.getContent().stream()
				.map(apiCallHistory -> modelMapper.map(apiCallHistory, ApiCallHistoryDTO.class))
				.toList();
		return new PaginationResponse<>(
				apiCallHistories,
				history.getNumber(),
				history.getSize(),
				history.getTotalElements(),
				history.getTotalPages());
	}

	public PaginationResponse<ApiCallHistoryDTO> getApiCallHistoryByMethod(String httpMethod, Pageable pageable) {
		var history = apiCallHistoryRepository.findByHttpMethodOrderByTimestampDesc(httpMethod, pageable);
		List<ApiCallHistoryDTO> apiCallHistories = history.getContent().stream()
				.map(apiCallHistory -> modelMapper.map(apiCallHistory, ApiCallHistoryDTO.class))
				.toList();
		return new PaginationResponse<>(
				apiCallHistories,
				history.getNumber(),
				history.getSize(),
				history.getTotalElements(),
				history.getTotalPages());
	}

	public PaginationResponse<ApiCallHistoryDTO> getApiCallHistoryByDateRange(LocalDateTime startDate,
			LocalDateTime endDate,
			Pageable pageable) {
		var history = apiCallHistoryRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate, pageable);
		List<ApiCallHistoryDTO> apiCallHistories = history.getContent().stream()
				.map(apiCallHistory -> modelMapper.map(apiCallHistory, ApiCallHistoryDTO.class))
				.toList();
		return new PaginationResponse<>(
				apiCallHistories,
				history.getNumber(),
				history.getSize(),
				history.getTotalElements(),
				history.getTotalPages());
	}
}
