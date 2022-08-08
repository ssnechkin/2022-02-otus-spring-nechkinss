package ru.rncb.dpec.service.dp.handbook;

import ru.rncb.dpec.domain.entity.dp.handbook.Actions;

import java.util.List;

public interface ActionsService {

    Actions add(String mnemonic, String name);

    Actions getById(long id);

    List<Actions> getAll();

    Actions edit(Actions actions, String mnemonic, String name);

    boolean delete(Actions actions);
}
