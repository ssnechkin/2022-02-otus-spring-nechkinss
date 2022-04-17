package ru.otus.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BookComment> comments = new ArrayList<>();
}
