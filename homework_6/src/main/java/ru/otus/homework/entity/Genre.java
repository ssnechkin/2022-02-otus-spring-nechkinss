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
@Table(name = "genre")
@SequenceGenerator(name = "SEQ_GENRE_ID", sequenceName = "sequence_genre_id", initialValue = 1, allocationSize = 1)
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENRE_ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "books")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
    private List<Book> books;
}
