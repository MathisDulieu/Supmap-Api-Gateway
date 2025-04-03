package com.novus.api_gateway.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class ContactMetrics {

    private final MeterRegistry meterRegistry;

    @Getter private final Timer supportEmailTimer;
    @Getter private final Timer subscribeNewsletterTimer;
    @Getter private final Timer unsubscribeNewsletterTimer;
    @Getter private final Timer sendNewsletterTimer;

    public ContactMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.supportEmailTimer = Timer.builder("contact.support_email.time")
                .description("Time taken to send support emails")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.subscribeNewsletterTimer = Timer.builder("contact.subscribe_newsletter.time")
                .description("Time taken to subscribe to newsletter")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.unsubscribeNewsletterTimer = Timer.builder("contact.unsubscribe_newsletter.time")
                .description("Time taken to unsubscribe from newsletter")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.sendNewsletterTimer = Timer.builder("contact.send_newsletter.time")
                .description("Time taken to send newsletter")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        Counter.builder("contact.operations.total")
                .description("Total number of contact operations")
                .register(meterRegistry);
    }

    public void recordOperation(String operationType, int status) {
        Counter.builder("contact.operations.total")
                .tag("operation", operationType)
                .tag("status", String.valueOf(status))
                .register(meterRegistry)
                .increment();
    }

    public <T> ResponseEntity<T> recordTimedOperation(Timer timer, String operationType, Supplier<ResponseEntity<T>> operation) {
        long startTime = System.nanoTime();
        ResponseEntity<T> response = operation.get();
        long duration = System.nanoTime() - startTime;

        timer.record(duration, TimeUnit.NANOSECONDS);

        recordOperation(operationType, response.getStatusCode().value());

        return response;
    }

    public void recordSupportEmail(String category) {
        Counter.builder("contact.support_emails")
                .tag("category", category)
                .register(meterRegistry)
                .increment();
    }

    public void recordNewsletterSubscription() {
        Counter.builder("contact.newsletter.subscribers")
                .register(meterRegistry)
                .increment();
    }

    public void recordNewsletterUnsubscription() {
        Counter.builder("contact.newsletter.unsubscribers")
                .register(meterRegistry)
                .increment();
    }

    public void recordNewsletterSent(int recipientCount) {
        Counter.builder("contact.newsletter.sent")
                .register(meterRegistry)
                .increment();

        Counter.builder("contact.newsletter.recipient_count")
                .register(meterRegistry)
                .increment(recipientCount);
    }
}