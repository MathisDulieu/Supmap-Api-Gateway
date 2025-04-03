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
public class NotificationMetrics {

    private final MeterRegistry meterRegistry;

    @Getter
    private final Timer updatePreferencesTimer;

    public NotificationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.updatePreferencesTimer = Timer.builder("notification.update_preferences.time")
                .description("Time taken to update notification preferences")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        Counter.builder("notification.operations.total")
                .description("Total number of notification operations")
                .register(meterRegistry);
    }

    public void recordOperation(String operationType, int status) {
        Counter.builder("notification.operations.total")
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

    public void recordPreferenceUpdate(String preferenceType, boolean enabled) {
        Counter.builder("notification.preference_updates")
                .tag("type", preferenceType)
                .tag("enabled", String.valueOf(enabled))
                .register(meterRegistry)
                .increment();
    }

    public void updateNotificationEnabledUsers(String notificationType, boolean increase) {
        Counter counter = Counter.builder("notification.enabled_users")
                .tag("type", notificationType)
                .register(meterRegistry);
        if (increase) {
            counter.increment();
        }
    }
}
