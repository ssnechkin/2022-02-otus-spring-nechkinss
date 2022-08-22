package ru.dpec.controller.dp.systems.parameterval.sub;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.in.dp.systems.parameterval.sub.ResponseFilerDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.systems.parameterval.sub.ResponseFilterUiService;

import java.util.List;

@RestController
public class ResponseFilterController {

    private final ResponseFilterUiService service;

    public ResponseFilterController(ResponseFilterUiService service) {
        this.service = service;
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValId")
    public Content list(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId) {
        return service.list(systemId, parameterValId);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdId")
    public Content view(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        return service.getContentView(systemId, parameterValId, id);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdId")
    public Content edit(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        return service.edit(systemId, parameterValId, id);
    }

    @PutMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdIdResponseFilerDto")
    public Content save(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id,
                        @RequestBody ResponseFilerDto responseFilerDto) {
        return service.save(systemId, parameterValId, id, responseFilerDto);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValId")
    public Content add(@PathVariable("system_id") long systemId,
                       @PathVariable("parameter_val_id") long parameterValId) {
        return service.add(systemId, parameterValId);
    }

    @PostMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdResponseFilerDto")
    public Content create(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @RequestBody ResponseFilerDto responseFilerDto) {
        return service.create(systemId, parameterValId, responseFilerDto);
    }

    @DeleteMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdId")
    public Content delete(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @PathVariable("id") long id) {
        return service.delete(systemId, parameterValId, id);
    }


    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackSystemIdParameterValId(long systemId, long parameterValId) {
        return fallback();
    }

    private Content fallbackSystemIdParameterValIdId(long systemId, long parameterValId, long id) {
        return fallback();
    }

    private Content fallbackSystemIdParameterValIdIdResponseFilerDto(long systemId, long parameterValId, long id,
                                                                     ResponseFilerDto responseFilerDto) {
        return fallback();
    }

    private Content fallbackSystemIdParameterValIdResponseFilerDto(long systemId, long parameterValId,
                                                                   ResponseFilerDto responseFilerDto) {
        return fallback();
    }
}