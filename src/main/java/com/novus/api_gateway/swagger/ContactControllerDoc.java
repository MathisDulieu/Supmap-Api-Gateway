package com.novus.api_gateway.swagger;

import com.novus.shared_models.request.Contact.SendNewsletterRequest;
import com.novus.shared_models.request.Contact.SendSupportEmailRequest;
import com.novus.shared_models.request.Contact.SubscribeToNewsletterRequest;
import com.novus.shared_models.request.Contact.UnsubscribeFromNewsletterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ContactControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Send support email",
            description = "Submits a support request to the customer service team. All required fields (email, subject, content) must be provided.",
            tags = {"Contact"}
    )
    @RequestBody(
            description = "Support request information",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SendSupportEmailRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Account issue",
                                    summary = "Support request for account access issue",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com",
                                          "subject": "Account Access Issue",
                                          "content": "I'm unable to access my account after changing my password. I've tried to reset it multiple times but I'm not receiving the reset email."
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Billing question",
                                    summary = "Support request for billing question",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu",
                                          "subject": "Question About Billing",
                                          "content": "I was charged twice for my subscription last month. Can you please help me resolve this and process a refund for the duplicate charge?"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Feature request",
                                    summary = "Support request for new feature suggestion",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io",
                                          "subject": "Feature Request",
                                          "content": "I would like to suggest adding dark mode to the application. This would greatly improve the user experience, especially for night-time users like myself."
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Technical issue",
                                    summary = "Support request for technical issue",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com",
                                          "subject": "Mobile App Crash",
                                          "content": "The mobile app keeps crashing when I try to upload a profile picture. I'm using an iPhone 13 with the latest iOS version and app version 2.4.0."
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "General inquiry",
                                    summary = "Support request for general information",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org",
                                          "subject": "Enterprise Plan Information",
                                          "content": "I'm interested in the enterprise plan for my company. We have approximately 200 employees. Could someone contact me with more information about pricing and features?"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Support request successfully submitted",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Your support request has been successfully submitted. Our team will contact you shortly at the provided email address."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Missing required fields",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Please fill in all required fields (email, subject, and content) to submit your support request."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An internal server error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface SendSupportEmailDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Subscribe to newsletter",
            description = "Subscribes an email address to the newsletter to receive updates and news about services.",
            tags = {"Contact"}
    )
    @RequestBody(
            description = "Email address for newsletter subscription",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubscribeToNewsletterRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user subscription example",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user subscription example",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user subscription example",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user subscription example",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user subscription example",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully subscribed to the newsletter",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Thank you for subscribing to our newsletter! You will now receive updates and news about our services."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email format",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Invalid email format. Please provide a valid email address."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An internal server error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface SubscribeToNewsletterDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Unsubscribe from newsletter",
            description = "Removes an email address from the newsletter subscription list. The user will no longer receive updates and news about services.",
            tags = {"Contact"}
    )
    @RequestBody(
            description = "Email address to unsubscribe from the newsletter",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnsubscribeFromNewsletterRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user unsubscription example",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user unsubscription example",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user unsubscription example",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user unsubscription example",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user unsubscription example",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully unsubscribed from the newsletter",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You have successfully unsubscribed from our newsletter. You will no longer receive emails from us."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email format",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Invalid email format. Please provide a valid email address."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Email not found in subscription list",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "The provided email is not subscribed to our newsletter."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An internal server error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface UnsubscribeFromNewsLetterDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Send newsletter to all subscribers",
            description = "Administrative endpoint to send a newsletter to all subscribed users. Requires admin authentication.",
            tags = {"Contact"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Newsletter content to be sent",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SendNewsletterRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Product update",
                                    summary = "Newsletter about product updates",
                                    value = """
                                        {
                                          "subject": "New Features Released - April 2025",
                                          "content": "We're excited to announce our latest features including improved dashboard analytics, mobile app enhancements, and new integration options. Check out our blog for more details on these exciting updates!"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Company news",
                                    summary = "Newsletter about company news",
                                    value = """
                                        {
                                          "subject": "Company Milestone: One Million Users!",
                                          "content": "We're thrilled to announce that we've reached one million users worldwide! To celebrate this milestone, we're offering a special discount for all premium subscriptions this month. Thank you for being part of our journey."
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Upcoming event",
                                    summary = "Newsletter about upcoming event",
                                    value = """
                                        {
                                          "subject": "Virtual Conference: Future of Technology - Register Now",
                                          "content": "Join us next month for our annual virtual conference featuring industry experts, interactive workshops, and networking opportunities. Early bird registration is now open with a 20% discount until April 15."
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Tips and tutorials",
                                    summary = "Newsletter with tips and tutorials",
                                    value = """
                                        {
                                          "subject": "5 Tips to Maximize Your Productivity with Our Platform",
                                          "content": "Discover hidden features and shortcuts that can help you work more efficiently. Our team has compiled the top 5 productivity hacks based on user feedback and usage patterns. Read on to become a power user!"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Holiday message",
                                    summary = "Seasonal holiday newsletter",
                                    value = """
                                        {
                                          "subject": "Holiday Hours and Special Offers",
                                          "content": "As we approach the holiday season, we want to inform you about our support hours during the holidays and share some special year-end offers exclusively for our newsletter subscribers."
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Newsletter successfully queued for sending",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Newsletter has been successfully queued for sending to all subscribers."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Missing required fields",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Both subject and content are required to send a newsletter. Please fill in all fields."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Authentication is required to access this resource."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You do not have sufficient permissions to send newsletters."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An internal server error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface SendNewsletterDoc {}

}
