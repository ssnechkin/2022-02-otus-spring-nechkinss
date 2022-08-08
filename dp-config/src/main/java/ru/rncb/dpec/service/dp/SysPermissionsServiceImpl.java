package ru.rncb.dpec.service.dp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.Systems;
import ru.rncb.dpec.repository.dp.SysPermissionsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SysPermissionsServiceImpl implements SysPermissionsService {

    private final SysPermissionsRepository repository;

    public SysPermissionsServiceImpl(SysPermissionsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SysPermissions add(Systems systems, Permissions permissions, List<SysResponse> sysResponseList, String comparing, String requestedDocumentsListName, String responsibleobject, long expire, long isDefault) {
        SysPermissions sysPermissions = new SysPermissions();
        sysPermissions.setSystems(systems);
        sysPermissions.setPermissions(permissions);
        sysPermissions.setComparing(comparing);
        sysPermissions.setRequestedDocumentsListName(requestedDocumentsListName);
        sysPermissions.setResponsibleobject(responsibleobject);
        sysPermissions.setExpire(expire);
        sysPermissions.setIsDefault(isDefault);
        sysPermissions.setSysResponseList(sysResponseList);
        return repository.save(sysPermissions);
    }

    @Override
    public SysPermissions getById(long id) {
        Optional<SysPermissions> sysPermissions = repository.findById(id);
        return sysPermissions.orElse(null);
    }

    @Override
    public List<SysPermissions> getAll() {
        return repository.findAll();
    }

    @Override
    public SysPermissions edit(SysPermissions sysPermissions, Systems systems, Permissions permissions, List<SysResponse> sysResponseList, String comparing, String requestedDocumentsListName, String responsibleobject, long expire, long isDefault) {
        sysPermissions.setSystems(systems);
        sysPermissions.setPermissions(permissions);
        sysPermissions.setComparing(comparing);
        sysPermissions.setRequestedDocumentsListName(requestedDocumentsListName);
        sysPermissions.setResponsibleobject(responsibleobject);
        sysPermissions.setExpire(expire);
        sysPermissions.setIsDefault(isDefault);
        sysPermissions.setSysResponseList(sysResponseList);
        return repository.save(sysPermissions);
    }

    @Override
    public boolean delete(SysPermissions sysPermissions) {
        repository.delete(sysPermissions);
        return true;
    }
}