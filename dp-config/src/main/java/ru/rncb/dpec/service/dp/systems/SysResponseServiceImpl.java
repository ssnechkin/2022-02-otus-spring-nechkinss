package ru.rncb.dpec.service.dp.systems;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.repository.dp.SysResponseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SysResponseServiceImpl implements SysResponseService {

    private final SysResponseRepository repository;

    public SysResponseServiceImpl(SysResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SysResponse add(SysPermissions sysPermissions, DocumentType documentType, String documentFactKey) {
        SysResponse sysResponse = new SysResponse();
        sysResponse.setDocumentType(documentType);
        sysResponse.setDocumentFactKey(documentFactKey == null || documentFactKey.isEmpty()
                ? "OriginalJSON" : documentFactKey);
        sysResponse.setSysPermissions(sysPermissions);
        return repository.save(sysResponse);
    }

    @Override
    public SysResponse getById(long id) {
        Optional<SysResponse> sysResponseMap = repository.findById(id);
        return sysResponseMap.orElse(null);
    }

    @Override
    public List<SysResponse> getAll() {
        return repository.findAll();
    }

    @Override
    public SysResponse edit(SysResponse sysResponse, DocumentType documentType, String documentFactKey) {
        sysResponse.setDocumentType(documentType);
        sysResponse.setDocumentFactKey(documentFactKey);
        return repository.save(sysResponse);
    }

    @Override
    public boolean delete(SysResponse sysResponse) {
        repository.delete(sysResponse);
        return true;
    }
}