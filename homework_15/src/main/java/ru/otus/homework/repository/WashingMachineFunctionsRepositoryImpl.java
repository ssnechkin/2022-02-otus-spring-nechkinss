package ru.otus.homework.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.WashingMachineFunction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WashingMachineFunctionsRepositoryImpl implements WashingMachineFunctionsRepository {
    private List<WashingMachineFunction> washingMachineFunctions = new CopyOnWriteArrayList<>();

    @Override
    public List<WashingMachineFunction> getAll() {
        return washingMachineFunctions;
    }

    @Override
    public Optional<WashingMachineFunction> getByName(String name) {
        return washingMachineFunctions.stream()
                .filter(wm -> wm.getName().equals(name))
                .findFirst();
    }

    @Override
    public WashingMachineFunction add(WashingMachineFunction washingMachineFunction) {
        Optional<WashingMachineFunction> wm = getByName(washingMachineFunction.getName());
        if (wm.isPresent()) {
            return wm.get();
        } else {
            washingMachineFunctions.add(washingMachineFunction);
            return washingMachineFunction;
        }
    }
}
