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

    @Column(name = "comment")
    private String comment;

    @Column(name = "genre")
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Genre> genre;

    @Column(name = "author")
    @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Author> author;
}
