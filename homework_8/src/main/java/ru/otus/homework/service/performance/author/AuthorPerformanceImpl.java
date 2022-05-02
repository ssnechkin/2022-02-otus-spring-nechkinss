package ru.otus.homework.service.performance.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.service.texter.author.AuthorTexter;
import ru.otus.homework.service.author.AuthorService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorPerformanceImpl implements AuthorPerformance{

    private final AuthorService authorService;
    private final AuthorTexter authorTexter;

    public AuthorPerformanceImpl(AuthorService authorService, AuthorTexter authorTexter) {
        this.authorService = authorService;
        this.authorTexter = authorTexter;
    }

    @Override
    public String add(String surname, String name, String patronymic) {
        Author author = authorService.add(surname, name, patronymic);
        return authorTexter.add(author);
    }

    @Override
    public List<String> getAll() {
        List<Author> authorList = authorService.getAll();
        ArrayList<String> result = new ArrayList<>();
        result.add(authorTexter.total(authorList.size()));
        for (Author author : authorList) {
            result.add(authorTexter.toString(author));
        }
        return result;
    }

    @Override
    public String edit(String id, String surname, String name, String patronymic) {
        Author author = authorService.getById(id);
        if (author == null) {
            return authorTexter.notFound(id);
        } else {
            author = authorService.edit(author, surname, name, patronymic);
            return authorTexter.edit(author);
        }
    }

    @Override
    public String deleteById(String id) {
        Author author = authorService.getById(id);
        if (author == null) {
            return authorTexter.notFound(id);
        } else {
            authorService.delete(author);
            return authorTexter.delete(id);
        }
    }
}