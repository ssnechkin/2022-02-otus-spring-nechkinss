package ru.otus.homework.service.performance.book;

import ru.otus.homework.service.performance.Performance;

import java.util.List;

public interface BookPerformance extends Performance {

    String add(String name);

    String getById(String id);

    String edit(String id, String name);

    String addComment(String id, String comment);

    List<String> getComments(String id);

    String editCommentByCommentId(String commentId, String comment);

    String deleteCommentByCommentId(String commentId);

    String addAuthor(String id, String authorId);

    String deleteAuthor(String id, String authorId);

    String addGenre(String id, String genreId);

    String deleteGenre(String id, String genreId);
}
