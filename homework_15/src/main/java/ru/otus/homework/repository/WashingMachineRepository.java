package ru.otus.homework.repository;

import ru.otus.homework.domain.WashingMachine;

import java.util.List;
import java.util.Optional;

public interface WashingMachineRepository {

    WashingMachine add(WashingMachine washingMachine);

    List<WashingMachine> getAll();

    Optional<WashingMachine> getByModel(String model);
}
