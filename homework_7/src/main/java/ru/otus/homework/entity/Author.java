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
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String surname;

    private String name;

    private String patronymic;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
