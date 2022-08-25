package ru.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.in.dp.handbook.PurposesDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.handbook.PurposesUiService;

import java.util.List;

@RestController
public class PurposesController {

    private final PurposesUiService service;

    public PurposesController(PurposesUiService service) {
        this.service = service;
    }

    @GetMapping("/handbook/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/handbook/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/handbook/purposes/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/handbook/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdPurposesDto")
    public Content save(@PathVariable("id") long id, @RequestBody PurposesDto purposesDto) {
        return service.save(id, purposesDto);
    }

    @GetMapping("/handbook/purposes/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/handbook/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPurposesDto")
    public Content create(@RequestBody PurposesDto purposesDto) {
        return service.create(purposesDto);
    }

    @DeleteMapping("/handbook/purposes/{id}")
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

    private Content fallbackIdPurposesDto(long id, PurposesDto purposesDto) {
        return fallback();
    }

    private Content fallbackPurposesDto(PurposesDto purposesDto) {
        return fallback();
    }


}