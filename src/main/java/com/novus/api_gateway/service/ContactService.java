package com.novus.api_gateway.service;

import com.novus.shared_models.request.Contact.SendSupportEmailRequest;
import com.novus.shared_models.request.Contact.SubscribeToNewsletterRequest;
import com.novus.shared_models.request.Contact.UnsubscribeFromNewsletterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {
    public ResponseEntity<String> sendSupportEmail(SendSupportEmailRequest request) {
        return null;
    }

    public ResponseEntity<String> subscribeToNewsletter(SubscribeToNewsletterRequest request) {
        return null;
    }

    public ResponseEntity<Void> unsubscribeFromNewsletter(UnsubscribeFromNewsletterRequest request) {
        return null;
    }

    public ResponseEntity<Void> sendNewsletter(SendNewsletterRequest request) {
        return null;
    }
}
