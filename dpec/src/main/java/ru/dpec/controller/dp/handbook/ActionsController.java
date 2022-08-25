package ru.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.handbook.ActionsUiService;
import ru.dpec.domain.dto.in.dp.handbook.ActionsDto;

import java.util.List;

@RestController
public class ActionsController {

    private final ActionsUiService service;

    public ActionsController(ActionsUiService service) {
        this.service = service;
    }

    @GetMapping("/handbook/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/handbook/actions/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdActionsDto")
    public Content save(@PathVariable("id") long id, @RequestBody ActionsDto actionsDto) {
        return service.save(id, actionsDto);
    }

    @GetMapping("/handbook/actions/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/handbook/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackActionsDto")
    public Content create(@RequestBody ActionsDto actionsDto) {
        return service.create(actionsDto);
    }

    @DeleteMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        return service.delete(id);
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackId(long id) {
        return fallback();
    }

    private Content fallbackIdActionsDto(long id, ActionsDto actionsDto) {
        return fallback();
    }

    private Content fallbackActionsDto(ActionsDto actionsDto) {
        return fallback();
    }
}