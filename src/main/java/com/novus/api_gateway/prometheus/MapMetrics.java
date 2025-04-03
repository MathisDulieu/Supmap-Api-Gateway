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
public class MapMetrics {

    private final MeterRegistry meterRegistry;

    @Getter private final Timer getMapAdminDashboardDataTimer;
    @Getter private final Timer saveNewAlertTimer;
    @Getter private final Timer getAllAlertsByPositionTimer;
    @Getter private final Timer getAllAlertsByRouteTimer;
    @Getter private final Timer getUserFavoriteLocationsTimer;
    @Getter private final Timer saveNewUserFavoriteLocationTimer;
    @Getter private final Timer deleteUserFavoriteLocationTimer;
    @Getter private final Timer updateUserFavoriteLocationTimer;
    @Getter private final Timer validateUserAlertTimer;
    @Getter private final Timer invalidateUserAlertTimer;
    @Getter private final Timer saveUserRouteTimer;
    @Getter private final Timer getUserRouteHistoryTimer;
    @Getter private final Timer updateUserNavigationPreferencesTimer;
    @Getter private final Timer getNearbyUsersTimer;
    @Getter private final Timer shareLocationTimer;
    @Getter private final Timer shareRouteTimer;
    @Getter private final Timer saveNewRouteRecalculationTimer;

    public MapMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.getMapAdminDashboardDataTimer = Timer.builder("map.get_dashboard_data.time")
                .description("Time taken to retrieve admin dashboard data")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.saveNewAlertTimer = Timer.builder("map.save_alert.time")
                .description("Time taken to save a new alert")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getAllAlertsByPositionTimer = Timer.builder("map.get_alerts_by_position.time")
                .description("Time taken to retrieve alerts by position")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getAllAlertsByRouteTimer = Timer.builder("map.get_alerts_by_route.time")
                .description("Time taken to retrieve alerts by route")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getUserFavoriteLocationsTimer = Timer.builder("map.get_favorite_locations.time")
                .description("Time taken to retrieve user's favorite locations")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.saveNewUserFavoriteLocationTimer = Timer.builder("map.save_favorite_location.time")
                .description("Time taken to save a new favorite location")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.deleteUserFavoriteLocationTimer = Timer.builder("map.delete_favorite_location.time")
                .description("Time taken to delete a favorite location")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.updateUserFavoriteLocationTimer = Timer.builder("map.update_favorite_location.time")
                .description("Time taken to update a favorite location")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.validateUserAlertTimer = Timer.builder("map.validate_alert.time")
                .description("Time taken to validate a user alert")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.invalidateUserAlertTimer = Timer.builder("map.invalidate_alert.time")
                .description("Time taken to invalidate a user alert")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.saveUserRouteTimer = Timer.builder("map.save_route.time")
                .description("Time taken to save a user route")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getUserRouteHistoryTimer = Timer.builder("map.get_route_history.time")
                .description("Time taken to retrieve a user's route history")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.updateUserNavigationPreferencesTimer = Timer.builder("map.update_navigation_preferences.time")
                .description("Time taken to update user's navigation preferences")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getNearbyUsersTimer = Timer.builder("map.get_nearby_users.time")
                .description("Time taken to get nearby users")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.shareLocationTimer = Timer.builder("map.share_location.time")
                .description("Time taken to share a location")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.shareRouteTimer = Timer.builder("map.share_route.time")
                .description("Time taken to share a route")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.saveNewRouteRecalculationTimer = Timer.builder("map.save_route_recalculation.time")
                .description("Time taken to save a new route recalculation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        Counter.builder("map.operations.total")
                .description("Total number of map operations")
                .register(meterRegistry);
    }

    public void recordOperation(String operationType, int status) {
        Counter.builder("map.operations.total")
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

    public void recordAlertOperation(String operationType) {
        Counter.builder("map.alert.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordFavoriteLocationOperation(String operationType) {
        Counter.builder("map.favorite_location.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordRouteOperation(String operationType) {
        Counter.builder("map.route.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordShareOperation(String operationType) {
        Counter.builder("map.share.operations")
                .tag("type", operationType)
                .register(meterRegistry)
                .increment();
    }

    public void recordAlertsByPositionSearch(double latitude, double longitude, int resultCount) {
        Counter.builder("map.alerts_position_search.total")
                .tag("lat_range", getLatitudeRange(latitude))
                .tag("long_range", getLongitudeRange(longitude))
                .register(meterRegistry)
                .increment();

        meterRegistry.summary("map.alerts_position_search.result_count").record(resultCount);
    }

    public void recordAlertsByRouteSearch(int resultCount) {
        Counter.builder("map.alerts_route_search.total")
                .register(meterRegistry)
                .increment();

        meterRegistry.summary("map.alerts_route_search.result_count").record(resultCount);
    }

    public void recordRouteRecalculation() {
        Counter.builder("map.route_recalculations.total")
                .register(meterRegistry)
                .increment();
    }

    public void recordNearbyUsersSearch(double latitude, double longitude, int resultCount) {
        Counter.builder("map.nearby_users_search.total")
                .tag("lat_range", getLatitudeRange(latitude))
                .tag("long_range", getLongitudeRange(longitude))
                .register(meterRegistry)
                .increment();

        meterRegistry.summary("map.nearby_users_search.result_count").record(resultCount);
    }

    private String getLatitudeRange(double latitude) {
        if (latitude < -60) return "south_polar";
        if (latitude < -30) return "south_temperate";
        if (latitude < 30) return "tropical";
        if (latitude < 60) return "north_temperate";
        return "north_polar";
    }

    private String getLongitudeRange(double longitude) {
        if (longitude < -135) return "far_west";
        if (longitude < -90) return "west";
        if (longitude < -45) return "mid_west";
        if (longitude < 0) return "near_west";
        if (longitude < 45) return "near_east";
        if (longitude < 90) return "mid_east";
        if (longitude < 135) return "east";
        return "far_east";
    }
}