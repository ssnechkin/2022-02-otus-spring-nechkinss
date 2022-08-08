package ru.rncb.dpec.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.rncb.dpec.repository.dp.handbook.DocumentTypeRepository;

@Component
public class HealthDocumentTypeEmpty implements HealthIndicator {
    private final DocumentTypeRepository documentTypeRepository;

    public HealthDocumentTypeEmpty(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public Health health() {
        if (documentTypeRepository.findAll().size() > 0) {
            return Health
                    .status(Status.UP)
                    .withDetail("message", "Ok")
                    .build();
        } else {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Document types are empty!")
                    .build();
        }
    }
}
