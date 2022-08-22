package ru.dpec.controller.dp.systems.parameterval.sub;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.in.dp.systems.parameterval.sub.RequestedDocumentsDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.systems.parameterval.sub.RequestedDocumentsUiService;

import java.util.List;

@RestController
public class RequestedDocumentsController {

    private final RequestedDocumentsUiService service;

    public RequestedDocumentsController(RequestedDocumentsUiService service) {
        this.service = service;
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValId")
    public Content list(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId) {
        return service.list(systemId, parameterValId);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdId")
    public Content view(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        return service.getContentView(systemId, parameterValId, id);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValId")
    public Content add(@PathVariable("system_id") long systemId,
                       @PathVariable("parameter_val_id") long parameterValId) {
        return service.add(systemId, parameterValId);
    }

    @PostMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemIdParameterValIdRequestedDocumentsDto")
    public Content create(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @RequestBody RequestedDocumentsDto requestedDocumentsDto) {
        return service.create(systemId, parameterValId, requestedDocumentsDto);
    }

    @DeleteMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/{id}")
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

    private Content fallbackSystemIdParameterValIdRequestedDocumentsDto(long systemId, long parameterValId,
                                                                        RequestedDocumentsDto requestedDocumentsDto) {
        return fallback();
    }
}