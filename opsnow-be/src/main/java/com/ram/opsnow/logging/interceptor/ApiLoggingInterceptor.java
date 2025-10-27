package com.ram.opsnow.logging.interceptor;

import com.ram.opsnow.logging.entity.ApiCallHistory;
import com.ram.opsnow.logging.service.ApiCallHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private final ApiCallHistoryService apiCallHistoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        try {
            long startTime = (Long) request.getAttribute("startTime");
            long duration = System.currentTimeMillis() - startTime;

            String userIdentifier = extractUserIdentifier();

            String requestBody = getRequestBody(request);

            String responseBody = getResponseBody(response);

            ApiCallHistory apiCallHistory = ApiCallHistory.builder()
                    .timestamp(LocalDateTime.now())
                    .apiEndpoint(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .userIdentifier(userIdentifier)
                    .responseStatus(response.getStatus())
                    .requestDurationMs(duration)
                    .clientIp(getClientIpAddress(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .requestBody(truncateIfNeeded(requestBody, 1000))
                    .responseBody(truncateIfNeeded(responseBody, 1000))
                    .build();

            apiCallHistoryService.logApiCall(apiCallHistory);

        } catch (Exception e) {
            log.error("Error in API logging interceptor: {}", e.getMessage(), e);
        }
    }

    private String extractUserIdentifier() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getName())) {
                return authentication.getName();
            }
        } catch (Exception e) {
            log.debug("Could not extract user identifier: {}", e.getMessage());
        }
        return null;
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            if (request.getContentLength() > 0 &&
                    (request.getContentType() != null && request.getContentType().contains("application/json"))) {
                return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.debug("Could not read request body: {}", e.getMessage());
        }
        return null;
    }

    private String getResponseBody(HttpServletResponse response) {
        try {
            if (response instanceof ContentCachingResponseWrapper wrapper) {
                byte[] content = wrapper.getContentAsByteArray();
                if (content.length > 0) {
                    return new String(content, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            log.debug("Could not read response body: {}", e.getMessage());
        }
        return null;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String truncateIfNeeded(String content, int maxLength) {
        if (content == null) {
            return null;
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "... [truncated]";
    }
}
