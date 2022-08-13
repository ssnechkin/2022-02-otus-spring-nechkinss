package ru.rncb.dpec.service.dp.permissions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.repository.dp.PermissionsRepository;
import ru.rncb.dpec.repository.dp.SysPermissionsRepository;
import ru.rncb.dpec.repository.dp.handbook.ActionsRepository;
import ru.rncb.dpec.repository.dp.handbook.PurposesRepository;
import ru.rncb.dpec.repository.dp.handbook.ScopeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsRepository repository;
    private final ActionsRepository actionsRepository;
    private final PurposesRepository purposesRepository;
    private final ScopeRepository scopeRepository;
    private final SysPermissionsRepository sysPermissionsRepository;

    public PermissionsServiceImpl(PermissionsRepository repository, ActionsRepository actionsRepository,
                                  PurposesRepository purposesRepository, ScopeRepository scopeRepository,
                                  SysPermissionsRepository sysPermissionsRepository) {
        this.repository = repository;
        this.actionsRepository = actionsRepository;
        this.purposesRepository = purposesRepository;
        this.scopeRepository = scopeRepository;
        this.sysPermissionsRepository = sysPermissionsRepository;
    }

    @Override
    @Transactional
    public Permissions add(String mnemonic, String name, String orgNameFio, long expire, String description) {
        Permissions permissions = new Permissions();
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setResponsibleobject(orgNameFio);
        permissions.setExpire(expire);
        permissions.setDescription(description);
        return repository.save(permissions);
    }

    @Override
    public Permissions getById(long id) {
        Optional<Permissions> permissions = repository.findById(id);
        return permissions.orElse(null);
    }

    @Override
    public List<Permissions> getAll() {
        return repository.findAll();
    }

    @Override
    public Permissions edit(Permissions permissions, String mnemonic, String name, String orgNameFio,
                            long expire, String description) {
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setResponsibleobject(orgNameFio);
        permissions.setExpire(expire);
        permissions.setDescription(description);
        return repository.save(permissions);
    }

    @Override
    public boolean addScope(Permissions permissions, Scope scope) {
        if (scope != null) {
            if (permissions.getScopeList() == null) {
                permissions.setScopeList(new ArrayList<>());
            }
            if (permissions.getScopeList().contains(scope)) {
                return false;
            }
            permissions.getScopeList().add(scope);
            scope.getPermissionsList().add(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean addPurposes(Permissions permissions, Purposes purposes) {
        if (purposes != null) {
            if (permissions.getPurposesList() == null) {
                permissions.setPurposesList(new ArrayList<>());
            }
            if (permissions.getPurposesList().contains(purposes)) {
                return false;
            }
            permissions.getPurposesList().add(purposes);
            purposes.getPermissionsList().add(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean addActions(Permissions permissions, Actions actions) {
        if (actions != null) {
            if (permissions.getActionsList() == null) {
                permissions.setActionsList(new ArrayList<>());
            }
            if (permissions.getActionsList().contains(actions)) {
                return false;
            }
            permissions.getActionsList().add(actions);
            actions.getPermissionsList().add(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteScope(Permissions permissions, Scope scope) {
        if (permissions != null && permissions.getScopeList() != null && permissions.getScopeList().contains(scope)) {
            permissions.getScopeList().remove(scope);
            scope.getPermissionsList().remove(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePurposes(Permissions permissions, Purposes purposes) {
        if (permissions != null
                && permissions.getPurposesList() != null
                && permissions.getPurposesList().contains(purposes)) {
            permissions.getPurposesList().remove(purposes);
            purposes.getPermissionsList().remove(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteActions(Permissions permissions, Actions actions) {
        if (permissions != null
                && permissions.getActionsList() != null
                && permissions.getActionsList().contains(actions)) {
            permissions.getActionsList().remove(actions);
            actions.getPermissionsList().remove(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Permissions permissions) {
        for (Actions actions : permissions.getActionsList()) {
            actions.getPermissionsList().remove(permissions);
            actionsRepository.save(actions);
        }
        for (Purposes purposes : permissions.getPurposesList()) {
            purposes.getPermissionsList().remove(permissions);
            purposesRepository.save(purposes);
        }
        for (Scope scope : permissions.getScopeList()) {
            scope.getPermissionsList().remove(permissions);
            scopeRepository.save(scope);
        }
        for (SysPermissions sysPermissions : sysPermissionsRepository.findAll()) {
            if (sysPermissions.getPermissions().equals(permissions)) {
                sysPermissionsRepository.delete(sysPermissions);
            }
        }
        repository.delete(permissions);
        return true;
    }
}