package ru.rncb.dpec.service.dp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.repository.dp.PermissionsRepository;

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
    public Permissions add(String mnemonic, String name, String description, long expire) {
        Permissions permissions = new Permissions();
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setDescription(description);
        permissions.setExpire(expire);
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
    public Permissions edit(Permissions permissions, String mnemonic, String name, String description, long expire) {
        permissions.setMnemonic(mnemonic);
        permissions.setName(name);
        permissions.setDescription(description);
        permissions.setExpire(expire);
        return repository.save(permissions);
    }

    @Override
    public boolean delete(Permissions permissions) {
        repository.delete(permissions);
        return true;
    }
}