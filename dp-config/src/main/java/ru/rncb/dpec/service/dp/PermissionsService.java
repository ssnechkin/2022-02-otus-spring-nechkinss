package ru.rncb.dpec.service.dp;

import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;

import java.util.List;

public interface PermissionsService {

    Permissions add(String mnemonic, String name, String description, long expire, String responsibleobject);

    Permissions getById(long id);

    List<Permissions> getAll();

    Permissions edit(Permissions permissions, String mnemonic, String name, String description, long expire, String responsibleobject);

    boolean delete(Permissions permissions);

    boolean addScope(Permissions permissions, long scopeId);

    boolean deleteScope(Permissions permissions, Scope scope);
}
