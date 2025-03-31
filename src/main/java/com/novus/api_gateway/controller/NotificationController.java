package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.NotificationService;
import com.novus.api_gateway.swagger.NotificationControllerDoc;
import com.novus.api_gateway.swagger.UserControllerDocs;
import com.novus.shared_models.request.Notification.UpdateAuthenticatedUserNotificationPreferencesRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping
    @NotificationControllerDoc.UpdateAuthenticatedUserDetailsDoc
    public ResponseEntity<String> updateAuthenticatedUserNotificationPreferences(
            @RequestBody UpdateAuthenticatedUserNotificationPreferencesRequest request
    ) {
        return notificationService.updateAuthenticatedUserNotificationPreferences(request);
    }

}
