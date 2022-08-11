package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.ui.handbook.HandbookUiService;

import java.util.List;

@RestController
public class HandbookController {

    private final HandbookUiService service;

    public HandbookController(HandbookUiService service) {
        this.service = service;
    }

    @GetMapping("/handbook")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }
}