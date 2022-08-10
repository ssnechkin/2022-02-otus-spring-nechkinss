package ru.rncb.dpec.service.dp.systems;

import ru.rncb.dpec.domain.entity.dp.Systems;

import java.util.List;

public interface SystemsService {

    Systems add(String name, String description);

    Systems getById(long id);

    List<Systems> getAll();

    Systems edit(Systems systems, String name, String description);

    boolean delete(Systems systems);
}
