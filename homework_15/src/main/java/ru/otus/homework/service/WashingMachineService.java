package ru.otus.homework.service;

import ru.otus.homework.domain.WashingMachine;
import ru.otus.homework.domain.WashingMachineFunction;

import java.util.List;
import java.util.Optional;

public interface WashingMachineService {

    List<WashingMachine> getAll();

    List<WashingMachineFunction> getAllWMFunctions();

    Optional<WashingMachine> getWashingMachineByModel(String model);

    List<WashingMachine> getAllModelsByFunction(String name);
}
