package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.integration.WashingMachineFactory;
import ru.otus.homework.service.io.IOServiceStreams;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

@Service
public class OrderServiceImpl implements OrderService {

    private final WashingMachineFactory washingMachineFactory;
    private final IOServiceStreams ioService;

    public OrderServiceImpl(WashingMachineFactory washingMachineFactory, IOServiceStreams ioService) {
        this.washingMachineFactory = washingMachineFactory;
        this.ioService = ioService;
    }

    @Override
    public void addOrders(String washingMachineModels) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        pool.execute(() -> {
            ioService.outputString("A new order for models was accepted: " + washingMachineModels);
            washingMachineFactory.process(Arrays.stream(washingMachineModels.split(",")).toList());
        });
    }
}
