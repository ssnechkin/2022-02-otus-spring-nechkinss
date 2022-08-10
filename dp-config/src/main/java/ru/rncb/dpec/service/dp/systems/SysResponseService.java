package ru.rncb.dpec.service.dp.systems;

import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;

import java.util.List;

public interface SysResponseService {

    SysResponse add(DocumentType documentType, String documentFactKey);

    SysResponse getById(long id);

    List<SysResponse> getAll();

    SysResponse edit(SysResponse sysResponse, DocumentType documentType, String documentFactKey);

    boolean delete(SysResponse sysResponse);
}
