package ru.otus.homework.service.texter.author;

import ru.otus.homework.domain.Author;
import ru.otus.homework.service.texter.Texter;

public interface AuthorTexter extends Texter<Author> {

    String toString(Author author);

    String edit(Author author);
}
