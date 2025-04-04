package com.novus.api_gateway.controller;

import com.novus.api_gateway.prometheus.NotificationMetrics;
import com.novus.api_gateway.service.NotificationService;
import com.novus.api_gateway.swagger.NotificationControllerDoc;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Notification.UpdateAuthenticatedUserNotificationPreferencesRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/notification/preferences")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMetrics metrics;

    @PutMapping
    @NotificationControllerDoc.UpdateAuthenticatedUserNotificationPreferencesDoc
    public ResponseEntity<String> updateAuthenticatedUserNotificationPreferences(
            @RequestBody UpdateAuthenticatedUserNotificationPreferencesRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUpdatePreferencesTimer(),
                "update_notification_preferences",
                () -> notificationService.updateAuthenticatedUserNotificationPreferences(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful() && request != null) {
            metrics.recordPreferenceUpdate("email", request.isEmail());
            metrics.updateNotificationEnabledUsers("email", request.isEmail());
        }

        return response;
    }
}