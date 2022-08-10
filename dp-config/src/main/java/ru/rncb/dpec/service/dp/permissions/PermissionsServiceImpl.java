package ru.rncb.dpec.service.dp.permissions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.repository.dp.PermissionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsRepository repository;

    public PermissionsServiceImpl(PermissionsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Permissions add(String mnemonic, String name, String description) {
        Permissions permissions = new Permissions();
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
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
    public Permissions edit(Permissions permissions, String mnemonic, String name, String description) {
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
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
        if (permissions != null && permissions.getPurposesList() != null && permissions.getPurposesList().contains(purposes)) {
            permissions.getPurposesList().remove(purposes);
            purposes.getPermissionsList().remove(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteActions(Permissions permissions, Actions actions) {
        if (permissions != null && permissions.getActionsList() != null && permissions.getActionsList().contains(actions)) {
            permissions.getActionsList().remove(actions);
            actions.getPermissionsList().remove(permissions);
            repository.save(permissions);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Permissions permissions) {
        repository.delete(permissions);
        return true;
    }
}