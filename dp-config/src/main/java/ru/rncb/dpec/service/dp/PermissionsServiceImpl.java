package ru.rncb.dpec.service.dp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.repository.dp.PermissionsRepository;
import ru.rncb.dpec.repository.dp.handbook.ScopeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsRepository repository;
    private final ScopeRepository scopeRepository;

    public PermissionsServiceImpl(PermissionsRepository repository, ScopeRepository scopeRepository) {
        this.repository = repository;
        this.scopeRepository = scopeRepository;
    }

    @Override
    @Transactional
    public Permissions add(String mnemonic, String name, String description, long expire, String responsibleobject) {
        Permissions permissions = new Permissions();
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setDescription(description);
        permissions.setExpire(expire);
        permissions.setResponsibleobject(responsibleobject);
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
    public Permissions edit(Permissions permissions, String mnemonic, String name, String description, long expire, String responsibleobject) {
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setDescription(description);
        permissions.setExpire(expire);
        permissions.setResponsibleobject(responsibleobject);
        return repository.save(permissions);
    }

    @Override
    public boolean addScope(Permissions permissions, long scopeId) {
        Optional<Scope> scope = scopeRepository.findById(scopeId);
        if (scope.isPresent()) {
            if (permissions.getScopeList() == null) {
                permissions.setScopeList(new ArrayList<>());
            }
            if(permissions.getScopeList().contains(scope.get())){
                return false;
            }
            permissions.getScopeList().add(scope.get());
            scope.get().getPermissionsList().add(permissions);
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
    public boolean delete(Permissions permissions) {
        repository.delete(permissions);
        return true;
    }
}