package ru.otus.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_comment")
@SequenceGenerator(name = "SEQ_BOOK_COMMENT_ID", sequenceName = "book_comment_id", initialValue = 1, allocationSize = 1)
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOK_COMMENT_ID")
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "comment")
    private String comment;
}
