package ru.otus.homework.domain.entity.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.genre.Genre;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<BookComment> comments = new ArrayList<>();
}
