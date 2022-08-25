package ru.dpec.service.dp.handbook;

import ru.dpec.domain.entity.dp.handbook.Scope;

import java.util.List;

public interface ScopeService {

    Scope add(String name, String description);

    Scope getById(long id);

    List<Scope> getAll();

    Scope edit(Scope scope, String name, String description);

    boolean delete(Scope scope);
}
