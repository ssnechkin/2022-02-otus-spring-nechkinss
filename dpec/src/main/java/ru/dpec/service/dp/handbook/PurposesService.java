package ru.dpec.service.dp.handbook;

import ru.dpec.domain.entity.dp.handbook.Purposes;

import java.util.List;

public interface PurposesService {

    Purposes add(String mnemonic, String name);

    Purposes getById(long id);

    List<Purposes> getAll();

    Purposes edit(Purposes purposes, String mnemonic, String name);

    boolean delete(Purposes purposes);
}
