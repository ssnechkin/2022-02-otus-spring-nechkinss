package ru.otus.homework.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.WashingMachine;
import ru.otus.homework.domain.WashingMachineFunction;
import ru.otus.homework.repository.WashingMachineRepository;
import ru.otus.homework.service.io.IOServiceStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FactoryServiceImpl implements FactoryService {
    private final WashingMachineRepository washingMachineRepository;
    private final IOServiceStreams ioService;

    public FactoryServiceImpl(WashingMachineRepository washingMachineRepository, IOServiceStreams ioService) {
        this.washingMachineRepository = washingMachineRepository;
        this.ioService = ioService;
    }

    @Override
    public boolean filterOutExistingModels(String model) {
        Optional<WashingMachine> washingMachineOptional = washingMachineRepository.getAll()
                .stream().filter(wm -> wm.getModel().equals(model)).findFirst();
        if (washingMachineOptional.isPresent()) {
            return true;
        } else {
            ioService.outputString("Non-existent model: " + model);
            return false;
        }
    }

    @Override
    public WashingMachine boxProduction(String model) {
        try {
            Thread.sleep(RandomUtils.nextInt(1000, 5000));
        } catch (InterruptedException ignore) {
        }
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setModel(model);
        ioService.outputString("Production of the box is completed for model: " + model);
        return washingMachine;
    }

    @Override
    public WashingMachine paintingOfTheModelBox(WashingMachine washingMachine) {
        try {
            Thread.sleep(RandomUtils.nextInt(1000, 5000));
        } catch (InterruptedException ignore) {
        }
        Optional<WashingMachine> wm = washingMachineRepository.getByModel(washingMachine.getModel());
        washingMachine.setColor(wm.get().getColor());
        washingMachine.setDoorColor(wm.get().getDoorColor());
        ioService.outputString("painting of the model box: "
                + washingMachine.getModel()
                + " color: " + washingMachine.getColor()
                + " doorColor: " + washingMachine.getDoorColor()
        );
        return washingMachine;
    }

    @Override
    public WashingMachine addTypeOfControl(WashingMachine washingMachine) {
        try {
            Thread.sleep(RandomUtils.nextInt(1000, 5000));
        } catch (InterruptedException ignore) {
        }
        Optional<WashingMachine> wm = washingMachineRepository.getByModel(washingMachine.getModel());
        washingMachine.setTypeOfControl(wm.get().getTypeOfControl());
        ioService.outputString("add type of control in model: "
                + washingMachine.getModel()
                + " type: " + washingMachine.getTypeOfControl()
        );
        return washingMachine;
    }

    @Override
    public WashingMachine addFunctions(WashingMachine washingMachine) {
        try {
            Thread.sleep(RandomUtils.nextInt(1000, 5000));
        } catch (InterruptedException ignore) {
        }
        Optional<WashingMachine> wm = washingMachineRepository.getByModel(washingMachine.getModel());
        List<WashingMachineFunction> wmfs = wm.get().getWmFunctions();
        washingMachine.setWmFunctions(new ArrayList<>());
        for (WashingMachineFunction wmf:wmfs){
            ioService.outputString("add function in model: "
                    + washingMachine.getModel()
                    + " name: " + wmf.getName()
            );
            washingMachine.getWmFunctions().add(wmf);
        }
        return washingMachine;
    }
}
