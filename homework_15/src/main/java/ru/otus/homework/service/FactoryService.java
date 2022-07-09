package ru.otus.homework.service;

import ru.otus.homework.domain.WashingMachine;

public interface FactoryService {
    boolean filterOutExistingModels(String model);
    WashingMachine boxProduction(String model);
    WashingMachine paintingOfTheModelBox(WashingMachine washingMachine);
    WashingMachine addTypeOfControl(WashingMachine washingMachine);
    WashingMachine addFunctions(WashingMachine washingMachine);
}
