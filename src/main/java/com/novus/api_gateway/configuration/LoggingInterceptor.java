package com.novus.api_gateway.configuration;

import com.novus.api_gateway.service.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final LoggingService loggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        long responseTime = System.currentTimeMillis() - startTime;

        String path = request.getRequestURI();
        String method = request.getMethod();
        int statusCode = response.getStatus();

        String serviceName = determineService(path);

        loggingService.logApiRequest(serviceName, path, method, statusCode, responseTime);
    }

    private String determineService(String path) {
        if (path.contains("/auth")) return "authentication-service";
        if (path.contains("/user")) return "user-service";
        if (path.contains("/notification")) return "notification-service";
        if (path.contains("/map")) return "map-service";
        if (path.contains("/contact")) return "contact-service";
        return "api-gateway";
    }

}
