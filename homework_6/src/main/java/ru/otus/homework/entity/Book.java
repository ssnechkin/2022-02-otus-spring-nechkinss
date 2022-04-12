package ru.otus.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@SequenceGenerator(name = "SEQ_BOOK_ID", sequenceName = "sequence_book_id", initialValue = 1, allocationSize = 1)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOK_ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "genres")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Genre> genres;

    @Column(name = "authors")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors;

    @Column(name = "comments")
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BookComment> comments;
}
