package ru.dpec.service.dp.permissions;

import ru.dpec.domain.entity.dp.Permissions;
import ru.dpec.domain.entity.dp.handbook.Actions;
import ru.dpec.domain.entity.dp.handbook.Purposes;
import ru.dpec.domain.entity.dp.handbook.Scope;

import java.util.List;

public interface PermissionsService {

    Permissions add(String mnemonic, String name, String orgNameFio, long expire, String description);

    Permissions getById(long id);

    List<Permissions> getAll();

    Permissions edit(Permissions permissions, String mnemonic, String name, String orgNameFio,
                     long expire, String description);

    boolean delete(Permissions permissions);

    boolean addScope(Permissions permissions, Scope scope);

    boolean addPurposes(Permissions permissions, Purposes purposes);

    boolean addActions(Permissions permissions, Actions actions);

    boolean deleteScope(Permissions permissions, Scope scope);

    boolean deletePurposes(Permissions permissions, Purposes purposes);

    boolean deleteActions(Permissions permissions, Actions actions);
}
