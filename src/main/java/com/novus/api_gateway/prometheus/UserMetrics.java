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
public class UserMetrics {

    private final MeterRegistry meterRegistry;

    @Getter private final Timer getUserDetailsTimer;
    @Getter private final Timer deleteUserTimer;
    @Getter private final Timer uploadProfileImageTimer;
    @Getter private final Timer updateUserDetailsTimer;
    @Getter private final Timer createAdminTimer;
    @Getter private final Timer deleteAdminTimer;
    @Getter private final Timer getAllUsersTimer;
    @Getter private final Timer getDashboardDataTimer;
    @Getter private final Timer rateApplicationTimer;
    @Getter private final Timer updateLocationTimer;

    public UserMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.getUserDetailsTimer = Timer.builder("user.get_details.time")
                .description("Time taken to retrieve user details")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.deleteUserTimer = Timer.builder("user.delete.time")
                .description("Time taken to delete a user account")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.uploadProfileImageTimer = Timer.builder("user.upload_image.time")
                .description("Time taken to upload a profile image")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.updateUserDetailsTimer = Timer.builder("user.update_details.time")
                .description("Time taken to update user details")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.createAdminTimer = Timer.builder("user.create_admin.time")
                .description("Time taken to create an admin account")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.deleteAdminTimer = Timer.builder("user.delete_admin.time")
                .description("Time taken to delete an admin account")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getAllUsersTimer = Timer.builder("user.get_all_users.time")
                .description("Time taken to retrieve all users")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getDashboardDataTimer = Timer.builder("user.get_dashboard_data.time")
                .description("Time taken to retrieve admin dashboard data")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.rateApplicationTimer = Timer.builder("user.rate_application.time")
                .description("Time taken to process application rating")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.updateLocationTimer = Timer.builder("user.update_location.time")
                .description("Time taken to update user location")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        Counter.builder("user.operations.total")
                .description("Total number of user operations")
                .register(meterRegistry);
    }

    public void recordOperation(String operationType, int status) {
        Counter.builder("user.operations.total")
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

    public void recordProfileOperation(String operationType) {
        Counter.builder("user.profile.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordAdminOperation(String operationType) {
        Counter.builder("user.admin.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordApplicationRating(int rating) {
        Counter.builder("user.app_rating.count")
                .tag("rating", String.valueOf(rating))
                .register(meterRegistry)
                .increment();

        Counter.builder("user.app_rating.sum")
                .register(meterRegistry)
                .increment(rating);
    }

    public void recordLocationUpdate() {
        Counter.builder("user.location_updates.total")
                .register(meterRegistry)
                .increment();
    }

    public void recordUserSearch(String keyword, int resultCount) {
        Counter.builder("user.searches.total")
                .tag("has_keyword", keyword != null && !keyword.isEmpty() ? "true" : "false")
                .register(meterRegistry)
                .increment();

        meterRegistry.summary("user.search.result_count").record(resultCount);
    }

}