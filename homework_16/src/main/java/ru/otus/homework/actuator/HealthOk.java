package ru.otus.homework.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class HealthOk implements HealthIndicator {
    @Override
    public Health health() {
        return Health
                .status(Status.UP)
                .status("OK")
                .withDetail("Health check","is alive")
                .build();
    }
}
