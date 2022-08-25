package ru.dpec.service.dp.handbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpec.domain.entity.dp.handbook.Scope;
import ru.dpec.repository.dp.handbook.ScopeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository repository;

    public ScopeServiceImpl(ScopeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Scope add(String name, String description) {
        Scope scope = new Scope();
        scope.setName(name);
        scope.setDescription(description);
        return repository.save(scope);
    }

    @Override
    public Scope getById(long id) {
        Optional<Scope> scope = repository.findById(id);
        return scope.orElse(null);
    }

    @Override
    public List<Scope> getAll() {
        return repository.findAll();
    }

    @Override
    public Scope edit(Scope scope, String name, String description) {
        scope.setName(name);
        scope.setDescription(description);
        return repository.save(scope);
    }

    @Override
    public boolean delete(Scope scope) {
        repository.delete(scope);
        return true;
    }
}