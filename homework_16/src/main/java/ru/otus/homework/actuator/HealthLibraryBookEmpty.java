package ru.otus.homework.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.homework.repository.book.BookRepository;

@Component
public class HealthLibraryBookEmpty implements HealthIndicator {
    private final BookRepository bookRepository;

    public HealthLibraryBookEmpty(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        if (bookRepository.findAll().size() > 0) {
            return Health
                    .status(Status.UP)
                    .withDetail("message", "There are books in the library")
                    .build();
        } else {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "The library of books is empty!")
                    .build();
        }
    }
}
