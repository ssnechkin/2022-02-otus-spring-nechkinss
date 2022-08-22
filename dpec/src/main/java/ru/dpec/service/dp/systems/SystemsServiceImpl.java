package ru.dpec.service.dp.systems;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpec.domain.entity.dp.Systems;
import ru.dpec.repository.dp.SystemsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SystemsServiceImpl implements SystemsService {

    private final SystemsRepository repository;

    public SystemsServiceImpl(SystemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Systems add(String name, String description) {
        Systems systems = new Systems();
        systems.setName(name);
        systems.setDescription(description);
        return repository.save(systems);
    }

    @Override
    public Systems getById(long id) {
        Optional<Systems> systems = repository.findById(id);
        return systems.orElse(null);
    }

    @Override
    public List<Systems> getAll() {
        return repository.findAll();
    }

    @Override
    public Systems edit(Systems systems, String name, String description) {
        systems.setName(name);
        systems.setDescription(description);
        return repository.save(systems);
    }

    @Override
    public boolean delete(Systems systems) {
        repository.delete(systems);
        return true;
    }
}