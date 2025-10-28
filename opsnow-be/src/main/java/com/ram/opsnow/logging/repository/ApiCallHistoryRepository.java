package com.ram.opsnow.logging.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ram.opsnow.logging.entity.ApiCallHistory;

@Repository
public interface ApiCallHistoryRepository extends JpaRepository<ApiCallHistory, String> {

	Page<ApiCallHistory> findByUserIdentifierOrderByTimestampDesc(String userIdentifier, Pageable pageable);

	Page<ApiCallHistory> findByApiEndpointContainingIgnoreCaseOrderByTimestampDesc(String endpoint,
			Pageable pageable);

	Page<ApiCallHistory> findByHttpMethodOrderByTimestampDesc(String httpMethod, Pageable pageable);

	@Query("SELECT a FROM ApiCallHistory a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
	Page<ApiCallHistory> findByTimestampBetweenOrderByTimestampDesc(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate,
			Pageable pageable);
}
