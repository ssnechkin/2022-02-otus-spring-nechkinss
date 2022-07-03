package ru.otus.homework.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.WashingMachine;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WashingMachineRepositoryImpl implements WashingMachineRepository {
    private List<WashingMachine> washingMachines = new CopyOnWriteArrayList<>();

    @Override
    public WashingMachine add(WashingMachine washingMachine) {
        Optional<WashingMachine> wm = getByModel(washingMachine.getModel());
        if (wm.isPresent()) {
            return wm.get();
        } else {
            washingMachines.add(washingMachine);
            return washingMachine;
        }
    }

    @Override
    public List<WashingMachine> getAll() {
        return washingMachines;
    }

    @Override
    public Optional<WashingMachine> getByModel(String model) {
        return washingMachines.stream()
                .filter(wm -> wm.getModel().equals(model))
                .findFirst();
    }
}
