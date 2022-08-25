package ru.dpec.controller.dp.systems.parameterval;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.in.dp.systems.parameterval.SystemsUrlParameterValDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.systems.parameterval.SystemsSysPermissionsUiService;

import java.util.List;

@RestController
public class SystemsSysPermissionsController {

    private final SystemsSysPermissionsUiService service;

    public SystemsSysPermissionsController(SystemsSysPermissionsUiService service) {
        this.service = service;
    }

    @GetMapping("/systems/{systems_id}/url_parameter/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsId")
    public Content addUrlParameterVal(@PathVariable("systems_id") long systemsId) {
        return service.addUrlParameterVal(systemsId);
    }

    @GetMapping("/systems/{systems_id}/parameter_val/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsIdId")
    public Content view(@PathVariable("systems_id") long systemsId, @PathVariable("id") long id) {
        return service.getContentView(systemsId, id);
    }

    @PostMapping("/systems/{systems_id}/parameter_val")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsSysPermissionsDto")
    public Content createParamVal(@PathVariable("systems_id") long systemsId,
                                  @RequestBody SystemsUrlParameterValDto urlParameterValDto) {
        return service.createParamVal(systemsId, urlParameterValDto);
    }

    @GetMapping("/systems/{systems_id}/parameter_val/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsIdId")
    public Content edit(@PathVariable("systems_id") long systemsId, @PathVariable("id") long id) {
        return service.edit(systemsId, id);
    }

    @PutMapping("/systems/{systems_id}/parameter_val/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdSystemsSysPermissionsDto")
    public Content save(@PathVariable("systems_id") long systemsId,
                        @PathVariable("id") long id,
                        @RequestBody SystemsUrlParameterValDto urlParameterValDto) {
        return service.save(systemsId, id, urlParameterValDto);
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackSystemsId(long systemsId) {
        return fallback();
    }

    private Content fallbackSystemsIdId(long systemsId, long id) {
        return fallback();
    }

    private Content fallbackSystemsSysPermissionsDto(long systemsId,
                                                     SystemsUrlParameterValDto systemsUrlParameterValDto) {
        return fallback();
    }

    private Content fallbackIdSystemsSysPermissionsDto(long systemsId, long id,
                                                       SystemsUrlParameterValDto systemsUrlParameterValDto) {
        return fallback();
    }
}