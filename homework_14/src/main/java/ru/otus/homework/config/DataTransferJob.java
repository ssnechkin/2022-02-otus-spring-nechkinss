package ru.otus.homework.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.mongo.Author;
import ru.otus.homework.domain.mongo.Genre;
import ru.otus.homework.domain.mongo.book.Book;
import ru.otus.homework.repository.relational.author.AuthorRepositoryRel;
import ru.otus.homework.repository.relational.book.BookRepositoryRel;
import ru.otus.homework.repository.relational.book.comment.BookCommentRepositoryRel;
import ru.otus.homework.repository.relational.genre.GenreRepositoryRel;
import ru.otus.homework.service.AuthorBatchService;
import ru.otus.homework.service.CleanUpService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DataTransferJob {
    public static final String JOB_NAME = "DataTransferJob";
    private static final int CHUNK_SIZE = 5;
    public final JobBuilderFactory jobBuilderFactory;
    public final AuthorBatchService authorBatchService;
    public final MongoTemplate mongoTemplate;
    public final StepBuilderFactory stepBuilderFactory;
    public final CleanUpService cleanUpService;
    public final AuthorRepositoryRel authorRepositoryRel;
    public final ru.otus.homework.repository.mongo.AuthorRepository authorRepository;
    public final GenreRepositoryRel genreRepositoryRel;
    public final ru.otus.homework.repository.mongo.GenreRepository genreRepository;
    public final BookRepositoryRel bookRepositoryRel;
    public final BookCommentRepositoryRel bookCommentRepositoryRel;

    public DataTransferJob(JobBuilderFactory jobBuilderFactory, AuthorBatchService authorBatchService, MongoTemplate mongoTemplate, StepBuilderFactory stepBuilderFactory, CleanUpService cleanUpService, AuthorRepositoryRel authorRepositoryRel, ru.otus.homework.repository.mongo.AuthorRepository authorRepository, GenreRepositoryRel genreRepositoryRel, ru.otus.homework.repository.mongo.GenreRepository genreRepository, BookRepositoryRel bookRepositoryRel, BookCommentRepositoryRel bookCommentRepositoryRel) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.authorBatchService = authorBatchService;
        this.mongoTemplate = mongoTemplate;
        this.stepBuilderFactory = stepBuilderFactory;
        this.cleanUpService = cleanUpService;
        this.authorRepositoryRel = authorRepositoryRel;
        this.authorRepository = authorRepository;
        this.genreRepositoryRel = genreRepositoryRel;
        this.genreRepository = genreRepository;
        this.bookRepositoryRel = bookRepositoryRel;
        this.bookCommentRepositoryRel = bookCommentRepositoryRel;
    }

    public Job getAuthorJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorStep())
                .next(transformGenreStep())
                .next(transformBookStep())
                .next(cleanUpStep())
                .end()
                .listener(authorBatchService.getJobExecutionListener())
                .build();
    }

    private Step transformAuthorStep() {
        return stepBuilderFactory.get("AuthorStep")
                .<Author, ru.otus.homework.domain.relational.Author>chunk(CHUNK_SIZE)
                .reader(readerAuthor())
                .processor(convertAuthorProcessor())
                .writer(writerAuthor())
                .build();
    }

    private Step transformGenreStep() {
        return stepBuilderFactory.get("GenreStep")
                .<Genre, ru.otus.homework.domain.relational.Genre>chunk(CHUNK_SIZE)
                .reader(readerGenre())
                .processor(convertGenreProcessor())
                .writer(writerGenre())
                .build();
    }

    private Step transformBookStep() {
        return stepBuilderFactory.get("BookStep")
                .<Book, ru.otus.homework.domain.relational.book.Book>chunk(CHUNK_SIZE)
                .reader(readerBook())
                .processor(convertBookProcessor())
                .writer(writerBook())
                .build();
    }

    @StepScope
    private MongoItemReader<Author> readerAuthor() {
        return new MongoItemReaderBuilder<Author>()
                .name("AuthorReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .collection("author")
                .targetType(Author.class)
                .sorts(Collections.emptyMap())
                .build();
    }

    @StepScope
    private MongoItemReader<Genre> readerGenre() {
        return new MongoItemReaderBuilder<Genre>()
                .name("GenreReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .collection("genre")
                .targetType(Genre.class)
                .sorts(Collections.emptyMap())
                .build();
    }

    @StepScope
    private MongoItemReader<Book> readerBook() {
        return new MongoItemReaderBuilder<Book>()
                .name("BookReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .collection("book")
                .targetType(Book.class)
                .sorts(Collections.emptyMap())
                .build();
    }

    @StepScope
    private ItemProcessor<Author, ru.otus.homework.domain.relational.Author> convertAuthorProcessor() {
        return this::convertAuthor;
    }

    @StepScope
    private ItemProcessor<Genre, ru.otus.homework.domain.relational.Genre> convertGenreProcessor() {
        return (genre) -> {
            ru.otus.homework.domain.relational.Genre genreRel = new ru.otus.homework.domain.relational.Genre();
            genreRel.setName(genre.getName());
            genreRel.setDescription(genre.getDescription());
            return genreRel;
        };
    }

    @StepScope
    private ItemProcessor<ru.otus.homework.domain.mongo.book.Book, ru.otus.homework.domain.relational.book.Book> convertBookProcessor() {
        return (book) -> {
            ru.otus.homework.domain.relational.book.Book bookRel = new ru.otus.homework.domain.relational.book.Book();
            bookRel.setName(book.getName());
            bookRel.setAuthors(getAuthors(book.getAuthorIdList()));
            bookRel.setGenres(getGenres(book.getGenreIdList()));
            return bookRel;
        };
    }

    private List<ru.otus.homework.domain.relational.Author> getAuthors(List<String> authorIds) {
        List<ru.otus.homework.domain.relational.Author> authors = new ArrayList<>();
        for (String authorId : authorIds) {
            Optional<Author> optionalAuthor = authorRepository.findById(authorId);
            if (optionalAuthor.isPresent()) {
                Author author = optionalAuthor.get();
                List<ru.otus.homework.domain.relational.Author> la = authorRepositoryRel.findByNameAndSurnameAndPatronymic(author.getName(), author.getSurname(), author.getPatronymic());
                authors.addAll(la);
            }
        }
        return authors;
    }

    private List<ru.otus.homework.domain.relational.Genre> getGenres(List<String> genreIds) {
        List<ru.otus.homework.domain.relational.Genre> genres = new ArrayList<>();
        for (String genreId : genreIds) {
            Optional<Genre> optionalGenre = genreRepository.findById(genreId);
            if (optionalGenre.isPresent()) {
                Genre genre = optionalGenre.get();
                List<ru.otus.homework.domain.relational.Genre> la = genreRepositoryRel.findByNameAndDescription(genre.getName(), genre.getDescription());
                genres.addAll(la);
            }
        }
        return genres;
    }

    @StepScope
    private ItemWriter<ru.otus.homework.domain.relational.Author> writerAuthor() {
        return authorRepositoryRel::saveAll;
    }

    @StepScope
    private ItemWriter<ru.otus.homework.domain.relational.Genre> writerGenre() {
        return genreRepositoryRel::saveAll;
    }

    @StepScope
    private ItemWriter<ru.otus.homework.domain.relational.book.Book> writerBook() {
        return bookRepositoryRel::saveAll;
    }

    private Step cleanUpStep() {
        return this.stepBuilderFactory.get("cleanUpStep")
                .tasklet(cleanUpTasklet())
                .build();
    }

    private MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");
        return adapter;
    }

    private ru.otus.homework.domain.relational.Author convertAuthor(ru.otus.homework.domain.mongo.Author author) {
        ru.otus.homework.domain.relational.Author authorRel = new ru.otus.homework.domain.relational.Author();
        authorRel.setName(author.getName());
        authorRel.setSurname(author.getSurname());
        authorRel.setPatronymic(author.getPatronymic());
        return authorRel;
    }
}
