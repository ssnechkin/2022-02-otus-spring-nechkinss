package ru.otus.homework.service.texter.genre;

import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.texter.Texter;

public interface GenreTexter extends Texter<Genre> {

    String toString(Genre genre);

    String edit(Genre genre);
}
