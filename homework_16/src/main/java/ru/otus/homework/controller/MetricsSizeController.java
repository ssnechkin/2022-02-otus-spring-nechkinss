package ru.otus.homework.controller;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.BaseUnits;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;
import ru.otus.homework.repository.genre.GenreRepository;

import java.util.Collection;

@RestController
public class MetricsSizeController {
    private final MeterRegistry meterRegistry;

    public MetricsSizeController(MeterRegistry meterRegistry, BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, BookCommentRepository bookCommentRepository) {
        this.meterRegistry = meterRegistry;

        Gauge.builder("book.size", bookRepository.findAll(), Collection::size)
                .baseUnit(BaseUnits.OBJECTS)
                .description("The number of all books")
                .register(meterRegistry);

        Gauge.builder("author.size", authorRepository.findAll(), Collection::size)
                .baseUnit(BaseUnits.OBJECTS)
                .description("The number of all authors")
                .register(meterRegistry);

        Gauge.builder("genre.size", genreRepository.findAll(), Collection::size)
                .baseUnit(BaseUnits.OBJECTS)
                .description("The number of all genres")
                .register(meterRegistry);

        Gauge.builder("book.comment.size", bookCommentRepository.findAll(), Collection::size)
                .baseUnit(BaseUnits.OBJECTS)
                .description("The number of all book comments")
                .register(meterRegistry);
    }

    @PostMapping("/books_size")
    public void booksSize() {
        meterRegistry.counter("book.size").increment();
    }

    @PostMapping("/authors_size")
    public void authorsSize() {
        meterRegistry.counter("author.size").increment();
    }

    @PostMapping("/genres_size")
    public void genresSize() {
        meterRegistry.counter("genre.size").increment();
    }

    @PostMapping("/book_comment_size")
    public void bookCommentsSize() {
        meterRegistry.counter("book.comment.size").increment();
    }
}
