package ru.rncb.dpec.service.dp.handbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.repository.dp.handbook.PurposesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PurposesServiceImpl implements PurposesService {

    private final PurposesRepository repository;

    public PurposesServiceImpl(PurposesRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Purposes add(String mnemonic, String name) {
        Purposes purposes = new Purposes();
        purposes.setMnemonic(mnemonic);
        purposes.setName(name);
        return repository.save(purposes);
    }

    @Override
    public Purposes getById(long id) {
        Optional<Purposes> purposes = repository.findById(id);
        return purposes.orElse(null);
    }

    @Override
    public List<Purposes> getAll() {
        return repository.findAll();
    }

    @Override
    public Purposes edit(Purposes purposes, String mnemonic, String name) {
        purposes.setMnemonic(mnemonic);
        purposes.setName(name);
        return repository.save(purposes);
    }

    @Override
    public boolean delete(Purposes purposes) {
        repository.delete(purposes);
        return true;
    }
}