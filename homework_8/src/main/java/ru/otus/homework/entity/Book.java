package ru.otus.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {

    @Id
    private String id;

    private String name;

    private List<Genre> genres = new ArrayList<>();

    private List<Author> authors = new ArrayList<>();

    private List<BookComment> comments = new ArrayList<>();
}
