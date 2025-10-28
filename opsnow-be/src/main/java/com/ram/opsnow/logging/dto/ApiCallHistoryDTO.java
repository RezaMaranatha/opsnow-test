package com.ram.opsnow.logging.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiCallHistoryDTO {
    private String id;
    private LocalDateTime timestamp;
    private String apiEndpoint;
    private String httpMethod;
    private String userIdentifier;
    private Integer responseStatus;
    private Long requestDurationMs;
    private String clientIp;
    private String userAgent;
    private String requestBody;
    private String responseBody;
}