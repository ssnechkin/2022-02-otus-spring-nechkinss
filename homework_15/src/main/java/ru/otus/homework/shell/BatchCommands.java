package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.WashingMachine;
import ru.otus.homework.domain.WashingMachineFunction;
import ru.otus.homework.service.OrderService;
import ru.otus.homework.service.WashingMachineService;
import ru.otus.homework.service.io.IOServiceStreams;

import java.util.Optional;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final WashingMachineService washingMachineService;
    private final OrderService orderService;
    private final IOServiceStreams ioService;

    @ShellMethod(value = "Print all WashingMachine", key = "all")
    public void printAllWashingMachine() throws Exception {
        for (WashingMachine wm : washingMachineService.getAll()) {
            ioService.outputString(wm.getModel());
        }
    }

    @ShellMethod(value = "Print all WashingMachine functions", key = "func")
    public void printAllWashingMachineFunctions() throws Exception {
        for (WashingMachineFunction wmf : washingMachineService.getAllWMFunctions()) {
            ioService.outputString(wmf.toString());
        }
    }

    @ShellMethod(value = "Print WashingMachine by model", key = "model")
    public void printAllWashingMachineByModel(@ShellOption String model) throws Exception {
        Optional<WashingMachine> wm = washingMachineService.getWashingMachineByModel(model);
        if (wm.isPresent()) {
            ioService.outputString(wm.toString());
        } else {
            ioService.outputString("model not found");
        }
    }

    @ShellMethod(value = "Print all WashingMachine by function", key = "allf")
    public void printAllWashingMachineByFunction(@ShellOption String functionName) throws Exception {
        for (WashingMachine wm : washingMachineService.getAllModelsByFunction(functionName)) {
            ioService.outputString(wm.toString());
        }
    }

    @ShellMethod(value = "Place an orders (entering comma-separated models)", key = "orders")
    public void placeAnOrders(@ShellOption String orders) throws Exception {
        orderService.addOrders(orders);
    }
}