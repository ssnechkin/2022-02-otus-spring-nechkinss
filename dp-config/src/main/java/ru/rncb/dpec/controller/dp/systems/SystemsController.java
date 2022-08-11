package ru.rncb.dpec.controller.dp.systems;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.in.dp.systems.SystemsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.ui.systems.SystemsUiService;

import java.util.List;

@RestController
public class SystemsController {

    private final SystemsUiService service;

    public SystemsController(SystemsUiService service) {
        this.service = service;
    }

    @GetMapping("/systems")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/systems/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdSystemsDto")
    public Content save(@PathVariable("id") long id, @RequestBody SystemsDto systemsDto) {
        return service.save(id, systemsDto);
    }

    @GetMapping("/systems/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/systems")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsDto")
    public Content create(@RequestBody SystemsDto systemsDto) {
        return service.create(systemsDto);
    }

    @DeleteMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        return service.delete(id);
    }

    @DeleteMapping("/systems/{systems_id}/parameter_val/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsIdId")
    public Content delete(@PathVariable("systems_id") long systemsId, @PathVariable("id") long id) {
        return service.delete(systemsId, id);
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

    private Content fallbackSystemsIdId(long systemsId, long id) {
        return fallback();
    }

    private Content fallbackIdSystemsDto(long id, SystemsDto systemsDto) {
        return fallback();
    }

    private Content fallbackSystemsDto(SystemsDto systemsDto) {
        return fallback();
    }
}