package ru.otus.homework.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.homework.domain.WashingMachine;
import ru.otus.homework.service.FactoryService;

@Configuration
public class IntegrationConfig {

    private static final int QUEUE_CAPACITY = 10;

    @Bean
    public QueueChannel ordersChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY).get();
    }

    @Bean
    public PublishSubscribeChannel washingMachineChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow factoryFlow(FactoryService factoryService) {
        return IntegrationFlows.from(ordersChannel())
                .split()
                .<String, Boolean>route(factoryService::filterOutExistingModels, sf -> sf
                        .subFlowMapping(true, order -> order
                                .handle(factoryService, "boxProduction")
                                .handle(factoryService, "paintingOfTheModelBox")
                                .handle(factoryService, "addTypeOfControl")
                                .handle(factoryService, "addFunctions")
                        ).subFlowMapping(false, order -> order
                                .handle(o -> MessageBuilder.withPayload(new WashingMachine()))
                        )
                )
                .aggregate()
                .log()
                .channel(washingMachineChannel())
                .get();
    }
}
