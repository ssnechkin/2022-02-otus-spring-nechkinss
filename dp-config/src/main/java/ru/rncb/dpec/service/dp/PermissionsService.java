package ru.rncb.dpec.service.dp;

import ru.rncb.dpec.domain.entity.dp.Permissions;

import java.util.List;

public interface PermissionsService {

    Permissions add(String mnemonic, String name, String description, long expire);

    Permissions getById(long id);

    List<Permissions> getAll();

    Permissions edit(Permissions permissions, String mnemonic, String name, String description, long expire);

    boolean delete(Permissions permissions);
}
