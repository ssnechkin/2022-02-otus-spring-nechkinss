package ru.dpec.service.dp.systems;

import ru.dpec.domain.entity.dp.Permissions;
import ru.dpec.domain.entity.dp.Systems;
import ru.dpec.domain.entity.dp.SysPermissions;

import java.util.List;

public interface SysPermissionsService {

    SysPermissions add(Systems systems, Permissions permissions, String urlParameterName,
                       String urlParameterValue, long expire, boolean isDefault);

    SysPermissions getById(long id);

    List<SysPermissions> getAll();

    SysPermissions edit(SysPermissions sysPermissions, Systems systems, Permissions permissions,
                        String urlParameterName, String urlParameterValue, long expire, boolean isDefault);

    boolean delete(SysPermissions sysPermissions);
}