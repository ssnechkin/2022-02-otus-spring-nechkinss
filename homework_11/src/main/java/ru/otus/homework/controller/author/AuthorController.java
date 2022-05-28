package ru.otus.homework.controller.author;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.repository.author.AuthorRepository;

@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/author")
    public Flux<AuthorDto> list() {
        return authorRepository.findAll()
                .map(this::toDto);
    }

    @GetMapping("/author/{id}")
    public Mono<ResponseEntity<AuthorDto>> view(@PathVariable("id") String id) {
        return authorRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/author")
    public Mono<ResponseEntity<AuthorDto>> add(@RequestBody AuthorDto authorDto) {
        return authorRepository.save(update(new Author(), authorDto))
                .map(this::toDto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/author/{id}")
    public Mono<ResponseEntity<AuthorDto>> save(@PathVariable("id") String id,
                                                @RequestBody AuthorDto authorDto) {
        return authorRepository.findById(id)
                .map(entity -> update(entity, authorDto))
                .map(this::save)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/author/{id}")
    public Mono<ResponseEntity<Boolean>> delete(@PathVariable("id") String id) {
        return authorRepository.findById(id)
                .map(this::deleteEntity)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    private Boolean deleteEntity(Author author) {
        if (author.getId() != null) {
            authorRepository.delete(author).subscribe();
            return true;
        }
        return null;
    }

    private AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getSurname(), author.getName(), author.getPatronymic());
    }

    private Author update(Author author, AuthorDto authorDto) {
        author.setSurname(authorDto.getSurname());
        author.setName(authorDto.getName());
        author.setPatronymic(authorDto.getPatronymic());
        return author;
    }

    private Author save(Author author) {
        authorRepository.save(author).subscribe();
        return author;
    }
}