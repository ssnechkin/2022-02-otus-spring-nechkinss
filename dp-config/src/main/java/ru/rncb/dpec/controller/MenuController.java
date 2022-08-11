package ru.rncb.dpec.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.security.UserDetail;
import ru.rncb.dpec.service.ui.MenuUiService;

import java.util.List;

@RestController
public class MenuController {

    private final MenuUiService service;

    public MenuController(MenuUiService service) {
        this.service = service;
    }

    @GetMapping("/menu")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content getMenu(@AuthenticationPrincipal UserDetail userDetail) {
        return service.getMenu(userDetail);
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }
}
