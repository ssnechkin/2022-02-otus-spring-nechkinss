package ru.dpec.service.dp.handbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpec.domain.entity.dp.handbook.Actions;
import ru.dpec.repository.dp.handbook.ActionsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ActionsServiceImpl implements ActionsService {

    private final ActionsRepository repository;

    public ActionsServiceImpl(ActionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Actions add(String mnemonic, String name) {
        Actions actions = new Actions();
        actions.setMnemonic(mnemonic);
        actions.setName(name);
        return repository.save(actions);
    }

    @Override
    public Actions getById(long id) {
        Optional<Actions> actions = repository.findById(id);
        return actions.orElse(null);
    }

    @Override
    public List<Actions> getAll() {
        return repository.findAll();
    }

    @Override
    public Actions edit(Actions actions, String mnemonic, String name) {
        actions.setMnemonic(mnemonic);
        actions.setName(name);
        return repository.save(actions);
    }

    @Override
    public boolean delete(Actions actions) {
        repository.delete(actions);
        return true;
    }
}