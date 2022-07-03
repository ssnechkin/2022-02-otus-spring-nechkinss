package ru.otus.homework.domain;

import lombok.Data;

import java.util.List;

@Data
public class WashingMachine {
    private String model, color, doorColor, typeOfControl;
    List<WashingMachineFunction> wmFunctions;
}
