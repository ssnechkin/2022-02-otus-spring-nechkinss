package ru.rncb.dpec.controller.dp.permissions;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsActionsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.ui.permissions.PermissionsActionsUiService;

import java.util.List;

@RestController
public class PermissionsActionsController {

    private final PermissionsActionsUiService service;

    public PermissionsActionsController(PermissionsActionsUiService service) {
        this.service = service;
    }

    @GetMapping("/permissions/{permissions_id}/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsId")
    public Content list(@PathVariable("permissions_id") long permissionsId) {
        return service.list(permissionsId);
    }

    @GetMapping("/permissions/{permissions_id}/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsIdId")
    public Content view(@PathVariable("permissions_id") long permissionsId, @PathVariable("id") long id) {
        return service.getContentView(permissionsId, id);
    }

    @GetMapping("/permissions/{permissions_id}/actions/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsId")
    public Content add(@PathVariable("permissions_id") long permissionsId) {
        return service.add(permissionsId);
    }

    @PostMapping("/permissions/{permissions_id}/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsIdPermissionsActionsDto")
    public Content create(@PathVariable("permissions_id") long permissionsId,
                          @RequestBody PermissionsActionsDto permissionsActionsDto) {
        return service.create(permissionsId, permissionsActionsDto);
    }

    @DeleteMapping("/permissions/{permissions_id}/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsIdId")
    public Content delete(@PathVariable("permissions_id") long permissionsId, @PathVariable("id") long id) {
        return service.delete(permissionsId, id);
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackPermissionsId(long permissionsId) {
        return fallback();
    }

    private Content fallbackPermissionsIdId(long permissionsId, long id) {
        return fallback();
    }

    private Content fallbackPermissionsIdPermissionsActionsDto(long permissionsId,
                                                               PermissionsActionsDto permissionsActionsDto) {
        return fallback();
    }
}