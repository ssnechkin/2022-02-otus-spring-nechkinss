package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.in.dp.handbook.ScopeDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.ui.handbook.ScopeUiService;

import java.util.List;

@RestController
public class ScopeController {

    private final ScopeUiService service;

    public ScopeController(ScopeUiService service) {
        this.service = service;
    }

    @GetMapping("/handbook/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/handbook/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/handbook/scope/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/handbook/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdScopeDto")
    public Content save(@PathVariable("id") long id, @RequestBody ScopeDto scopeDto) {
        return service.save(id, scopeDto);
    }

    @GetMapping("/handbook/scope/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/handbook/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackScopeDto")
    public Content create(@RequestBody ScopeDto scopeDto) {
        return service.create(scopeDto);
    }

    @DeleteMapping("/handbook/scope/{id}")
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

    private Content fallbackIdScopeDto(long id, ScopeDto scopeDto) {
        return fallback();
    }

    private Content fallbackScopeDto(ScopeDto scopeDto) {
        return fallback();
    }
}