package com.novus.api_gateway.service;

import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Notification.UpdateAuthenticatedUserNotificationPreferencesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    public ResponseEntity<String> updateAuthenticatedUserNotificationPreferences(UpdateAuthenticatedUserNotificationPreferencesRequest request, User authenticatedUser) {
        return null;
    }
}
