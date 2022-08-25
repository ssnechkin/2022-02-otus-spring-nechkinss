package ru.dpec.controller.dp.permissions;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.dto.in.dp.permissions.PermissionsPurposesDto;
import ru.dpec.service.ui.permissions.PermissionsPurposesUiService;

import java.util.List;

@RestController
public class PermissionsPurposesController {

    private final PermissionsPurposesUiService service;

    public PermissionsPurposesController(PermissionsPurposesUiService service) {
        this.service = service;
    }

    @GetMapping("/permissions/{permissions_id}/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsId")
    public Content list(@PathVariable("permissions_id") long permissionsId) {
        return service.list(permissionsId);
    }

    @GetMapping("/permissions/{permissions_id}/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsIdId")
    public Content view(@PathVariable("permissions_id") long permissionsId, @PathVariable("id") long id) {
        return service.getContentView(permissionsId, id);
    }

    @GetMapping("/permissions/{permissions_id}/purposes/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsId")
    public Content add(@PathVariable("permissions_id") long permissionsId) {
        return service.add(permissionsId);
    }

    @PostMapping("/permissions/{permissions_id}/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsIdPermissionsPurposesDto")
    public Content create(@PathVariable("permissions_id") long permissionsId,
                          @RequestBody PermissionsPurposesDto permissionsPurposesDto) {
        return service.create(permissionsId, permissionsPurposesDto);
    }

    @DeleteMapping("/permissions/{permissions_id}/purposes/{id}")
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

    private Content fallbackPermissionsIdPermissionsPurposesDto(long permissionsId,
                                                                PermissionsPurposesDto permissionsPurposesDto) {
        return fallback();
    }
}