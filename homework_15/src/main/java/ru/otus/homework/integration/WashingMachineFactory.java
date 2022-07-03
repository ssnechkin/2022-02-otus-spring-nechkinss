package ru.otus.homework.integration;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.domain.WashingMachine;

import java.util.Collection;

@MessagingGateway
public interface WashingMachineFactory {

    @Gateway(requestChannel = "ordersChannel", replyChannel = "washingMachineChannel")
    Collection<WashingMachine> process(Collection<String> models);
}
