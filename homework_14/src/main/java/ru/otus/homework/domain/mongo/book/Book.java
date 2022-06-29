package ru.otus.homework.domain.mongo.book;

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

    private List<String> genreIdList = new ArrayList<>();

    private List<String> authorIdList = new ArrayList<>();

    private List<String> commentIdList = new ArrayList<>();
}
