package ru.otus.homework.repository;

import ru.otus.homework.domain.WashingMachineFunction;

import java.util.List;
import java.util.Optional;

public interface WashingMachineFunctionsRepository {

    List<WashingMachineFunction> getAll();

    Optional<WashingMachineFunction> getByName(String name);

    WashingMachineFunction add(WashingMachineFunction washingMachineFunction);
}
