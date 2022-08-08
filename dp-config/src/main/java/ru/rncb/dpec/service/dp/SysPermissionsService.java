package ru.rncb.dpec.service.dp;

import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.Systems;

import java.util.List;

public interface SysPermissionsService {

    SysPermissions add(Systems systems, Permissions permissions, List<SysResponse> sysResponseList, String comparing, String requestedDocumentsListName, String responsibleobject, long expire, long isDefault);

    SysPermissions getById(long id);

    List<SysPermissions> getAll();

    SysPermissions edit(SysPermissions sysPermissions,Systems systems, Permissions permissions, List<SysResponse> sysResponseList, String comparing, String requestedDocumentsListName, String responsibleobject, long expire, long isDefault);

    boolean delete(SysPermissions sysPermissions);
}
