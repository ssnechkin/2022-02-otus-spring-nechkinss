package ru.dpec.service.dp.systems;

import ru.dpec.domain.entity.dp.SysResponse;
import ru.dpec.domain.entity.dp.handbook.DocumentType;
import ru.dpec.domain.entity.dp.SysPermissions;

import java.util.List;

public interface SysResponseService {

    SysResponse add(SysPermissions sysPermissions, DocumentType documentType, String documentFactKey);

    SysResponse getById(long id);

    List<SysResponse> getAll();

    SysResponse edit(SysResponse sysResponse, DocumentType documentType, String documentFactKey);

    boolean delete(SysResponse sysResponse);
}
