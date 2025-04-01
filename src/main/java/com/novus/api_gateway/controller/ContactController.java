package com.novus.api_gateway.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/contact/send-support-email")
    @ContactControllerDoc.SendSupportEmailDoc
    public ResponseEntity<String> sendSupportEmail(
            @RequestBody SendSupportEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        return contactService.sendSupportEmail(request, httpRequest);
    }

    @PostMapping("/contact/subscribe-newsletter")
    @ContactControllerDoc.SubscribeToNewsletterDoc
    public ResponseEntity<String> subscribeToNewsletter(
            @RequestBody SubscribeToNewsletterRequest request,
            HttpServletRequest httpRequest
    ) {
        return contactService.subscribeToNewsletter(request, httpRequest);
    }

    @PostMapping("/contact/unsubscribe-newsletter")
    @ContactControllerDoc.UnsubscribeFromNewsLetterDoc
    public ResponseEntity<String> unsubscribeFromNewsletter(
            @RequestBody UnsubscribeFromNewsletterRequest request,
            HttpServletRequest httpRequest
    ) {
        return contactService.unsubscribeFromNewsletter(request, httpRequest);
    }

    @PostMapping("/private/admin/send-newsletter")
    @ContactControllerDoc.SendNewsletterDoc
    public ResponseEntity<String> sendNewsletter(
            @RequestBody SendNewsletterRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return contactService.sendNewsletter(request, authenticatedUser, httpRequest);
    }

}
