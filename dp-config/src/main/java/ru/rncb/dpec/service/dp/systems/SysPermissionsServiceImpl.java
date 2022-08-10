package ru.rncb.dpec.service.dp.systems;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.Systems;
import ru.rncb.dpec.repository.dp.SysPermissionsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SysPermissionsServiceImpl implements SysPermissionsService {

    private final SysPermissionsRepository repository;

    public SysPermissionsServiceImpl(SysPermissionsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SysPermissions add(Systems systems, Permissions permissions, String comparing, String responsibleobject, long expire, boolean isDefault) {
        SysPermissions sysPermissions = new SysPermissions();
        sysPermissions.setSystems(systems);
        sysPermissions.setPermissions(permissions);
        sysPermissions.setComparing(comparing);
        sysPermissions.setResponsibleobject(responsibleobject);
        sysPermissions.setExpire(expire);
        if (isDefault) {
            omitTheDefaultFlagForEveryone(systems);
        }
        sysPermissions.setIsDefault(isDefault ? 1 : 0);
        sysPermissions.setRequestedDocumentsListName(systems.getName() + permissions.getMnemonic() + comparing + UUID.randomUUID());
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
    public SysPermissions edit(SysPermissions sysPermissions, Systems systems, Permissions permissions, String comparing, String responsibleobject, long expire, boolean isDefault) {
        sysPermissions.setSystems(systems);
        sysPermissions.setPermissions(permissions);
        sysPermissions.setComparing(comparing);
        sysPermissions.setResponsibleobject(responsibleobject);
        sysPermissions.setExpire(expire);
        if (isDefault) {
            omitTheDefaultFlagForEveryone(systems);
        }
        sysPermissions.setIsDefault(isDefault ? 1 : 0);
        return repository.save(sysPermissions);
    }

    @Override
    public boolean delete(SysPermissions sysPermissions) {
        repository.delete(sysPermissions);
        return true;
    }

    private void omitTheDefaultFlagForEveryone(Systems systems) {
        for (SysPermissions sysPermissions : systems.getSysPermissionsList()) {
            sysPermissions.setIsDefault(0);
            repository.save(sysPermissions);
        }
    }
}