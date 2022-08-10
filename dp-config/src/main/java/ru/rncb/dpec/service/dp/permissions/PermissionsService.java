package ru.rncb.dpec.service.dp.permissions;

import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;

import java.util.List;

public interface PermissionsService {

    Permissions add(String mnemonic, String name, String description);

    Permissions getById(long id);

    List<Permissions> getAll();

    Permissions edit(Permissions permissions, String mnemonic, String name, String description);

    boolean delete(Permissions permissions);

    boolean addScope(Permissions permissions, long scopeId);

    boolean deleteScope(Permissions permissions, Scope scope);
}
