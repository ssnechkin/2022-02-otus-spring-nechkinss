package ru.rncb.dpec.service.dp;

import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.SysResponse;

import java.util.List;

public interface SysResponseService {

    SysResponse add(SysPermissions sysPermissions, DocumentType documentType, String documentFactKey);

    SysResponse getById(long id);

    List<SysResponse> getAll();

    SysResponse edit(SysResponse sysResponse, SysPermissions sysPermissions, DocumentType documentType, String documentFactKey);

    boolean delete(SysResponse sysResponse);
}
