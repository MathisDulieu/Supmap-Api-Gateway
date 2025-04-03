package com.novus.api_gateway.controller;

import com.novus.api_gateway.prometheus.ContactMetrics;
import com.novus.api_gateway.service.ContactService;
import com.novus.api_gateway.swagger.ContactControllerDoc;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Contact.SendNewsletterRequest;
import com.novus.shared_models.request.Contact.SendSupportEmailRequest;
import com.novus.shared_models.request.Contact.SubscribeToNewsletterRequest;
import com.novus.shared_models.request.Contact.UnsubscribeFromNewsletterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final ContactMetrics metrics;

    @PostMapping("/contact/send-support-email")
    @ContactControllerDoc.SendSupportEmailDoc
    public ResponseEntity<String> sendSupportEmail(
            @RequestBody SendSupportEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSupportEmailTimer(),
                "send_support_email",
                () -> contactService.sendSupportEmail(request, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful() && request != null && request.getSubject() != null) {
            metrics.recordSupportEmail(request.getSubject());
        } else {
            metrics.recordSupportEmail("unknown");
        }

        return response;
    }

    @PostMapping("/contact/subscribe-newsletter")
    @ContactControllerDoc.SubscribeToNewsletterDoc
    public ResponseEntity<String> subscribeToNewsletter(
            @RequestBody SubscribeToNewsletterRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSubscribeNewsletterTimer(),
                "subscribe_newsletter",
                () -> contactService.subscribeToNewsletter(request, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordNewsletterSubscription();
        }

        return response;
    }

    @PostMapping("/contact/unsubscribe-newsletter")
    @ContactControllerDoc.UnsubscribeFromNewsLetterDoc
    public ResponseEntity<String> unsubscribeFromNewsletter(
            @RequestBody UnsubscribeFromNewsletterRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUnsubscribeNewsletterTimer(),
                "unsubscribe_newsletter",
                () -> contactService.unsubscribeFromNewsletter(request, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordNewsletterUnsubscription();
        }

        return response;
    }

    @PostMapping("/private/admin/contact/send-newsletter")
    @ContactControllerDoc.SendNewsletterDoc
    public ResponseEntity<String> sendNewsletter(
            @RequestBody SendNewsletterRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSendNewsletterTimer(),
                "send_newsletter",
                () -> contactService.sendNewsletter(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful() && request != null && request.getSubject() != null) {
            metrics.recordNewsletterSent(1);
        }

        return response;
    }
}