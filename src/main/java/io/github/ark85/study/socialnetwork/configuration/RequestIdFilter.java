package io.github.ark85.study.socialnetwork.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Request-Id";
    public static final String MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(HEADER_NAME);
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(MDC_KEY, requestId);
        response.setHeader(HEADER_NAME, requestId);
        try {
            if (canLog(request)) {
                log.info("Incoming {} {}", request.getMethod(), request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            if (canLog(request)) {
                log.info("Completed {} {} with status {}", request.getMethod(), request.getRequestURI(), response.getStatus());
            }
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    private boolean canLog(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/api/health-check") && !uri.startsWith("/actuator");
    }
}
