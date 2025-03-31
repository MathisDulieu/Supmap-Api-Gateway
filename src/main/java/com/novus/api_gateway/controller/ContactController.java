package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.ContactService;
import com.novus.api_gateway.swagger.ContactControllerDoc;
import com.novus.shared_models.request.Contact.SendSupportEmailRequest;
import com.novus.shared_models.request.Contact.SubscribeToNewsletterRequest;
import com.novus.shared_models.request.Contact.UnsubscribeFromNewsletterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/send-support-email")
    @ContactControllerDoc.SendSupportEmailDoc
    public ResponseEntity<String> sendSupportEmail(
            @RequestBody SendSupportEmailRequest request
    ) {
        return contactService.sendSupportEmail(request);
    }

    @PostMapping("/subscribe-newsletter")
    @ContactControllerDoc.SubscribeToNewsletterDoc
    public ResponseEntity<String> subscribeToNewsletter(
            @RequestBody SubscribeToNewsletterRequest request
    ) {
        return contactService.subscribeToNewsletter(request);
    }

    @PostMapping("/unsubscribe-newsletter")
    @ContactControllerDoc.UnsubscribeFromNewsLetterDoc
    public ResponseEntity<Void> unsubscribeFromNewsletter(
            @org.springframework.web.bind.annotation.RequestBody UnsubscribeFromNewsletterRequest request
    ) {
        return contactService.unsubscribeFromNewsletter(request);
    }

    @PostMapping("/private/admin/send-newsletter")
    @ContactControllerDoc.SendNewsletterDoc
    public ResponseEntity<Void> sendNewsletter(
            @RequestBody SendNewsletterRequest request
    ) {
        return contactService.sendNewsletter(request);
    }

}
