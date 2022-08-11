package ru.rncb.dpec.controller.dp.permissions;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.ui.permissions.PermissionsUiService;

import java.util.List;

@RestController
public class PermissionsController {

    private final PermissionsUiService service;

    public PermissionsController(PermissionsUiService service) {
        this.service = service;
    }

    @GetMapping("/permissions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/permissions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/permissions/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/permissions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdPermissionsDto")
    public Content save(@PathVariable("id") long id, @RequestBody PermissionsDto permissionsDto) {
        return service.save(id, permissionsDto);
    }

    @GetMapping("/permissions/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/permissions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsDto")
    public Content create(@RequestBody PermissionsDto permissionsDto) {
        return service.create(permissionsDto);
    }

    @DeleteMapping("/permissions/{id}")
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

    private Content fallbackIdPermissionsDto(long id, PermissionsDto permissionsDto) {
        return fallback();
    }

    private Content fallbackPermissionsDto(PermissionsDto permissionsDto) {
        return fallback();
    }
}