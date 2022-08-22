package ru.dpec.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.dto.in.UserDto;
import ru.dpec.service.ui.administration.UsersUiService;

import java.util.List;

@RestController
public class UsersController {

    private final UsersUiService service;

    public UsersController(UsersUiService service) {
        this.service = service;
    }

    @GetMapping("/users")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/users/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/users/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/users/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdUserDto")
    public Content save(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        return service.save(id, userDto);
    }

    @GetMapping("/users/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/users")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackUserDto")
    public Content create(@RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @DeleteMapping("/users/{id}")
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

    private Content fallbackIdUserDto(long id, UserDto userDto) {
        return fallback();
    }

    private Content fallbackUserDto(UserDto userDto) {
        return fallback();
    }
}