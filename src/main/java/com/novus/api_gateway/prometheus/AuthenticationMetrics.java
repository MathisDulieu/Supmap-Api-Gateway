package com.novus.api_gateway.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMetrics {

    private final MeterRegistry meterRegistry;

    @Getter
    private final Timer registerTimer;
    @Getter
    private final Timer loginTimer;
    @Getter
    private final Timer confirmEmailTimer;
    @Getter
    private final Timer forgotPasswordTimer;
    @Getter
    private final Timer resetPasswordTimer;
    @Getter
    private final Timer oauthLoginTimer;

    public AuthenticationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.registerTimer = Timer.builder("auth.register.time")
                .description("Time taken to process registration requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.loginTimer = Timer.builder("auth.login.time")
                .description("Time taken to process login requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.confirmEmailTimer = Timer.builder("auth.confirm_email.time")
                .description("Time taken to process email confirmations")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.forgotPasswordTimer = Timer.builder("auth.forgot_password.time")
                .description("Time taken to process forgot password requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.resetPasswordTimer = Timer.builder("auth.reset_password.time")
                .description("Time taken to process password reset requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.oauthLoginTimer = Timer.builder("auth.oauth_login.time")
                .description("Time taken to process OAuth login requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        Counter.builder("auth.operations.total")
                .description("Total number of authentication operations")
                .register(meterRegistry);
    }

    public void recordOperation(String operationType, int status) {
        Counter.builder("auth.operations.total")
                .tag("operation", operationType)
                .tag("status", String.valueOf(status))
                .register(meterRegistry)
                .increment();
    }

    public void recordAuthenticationType(String authType) {
        Counter.builder("auth.method.usage")
                .tag("type", authType)
                .register(meterRegistry)
                .increment();
    }

    public void recordSuccessfulAuth() {
        Counter.builder("auth.success.total")
                .register(meterRegistry)
                .increment();
    }

    public void recordFailedAuth() {
        Counter.builder("auth.failed.total")
                .register(meterRegistry)
                .increment();
    }

}
