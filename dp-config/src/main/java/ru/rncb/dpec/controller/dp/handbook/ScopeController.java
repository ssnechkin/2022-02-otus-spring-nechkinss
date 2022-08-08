package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.dto.in.dp.handbook.ScopeDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.handbook.ScopeService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScopeController {

    private final ScopeService service;
    private final static String PAGE_NAME = "Области доступа (Scope)";

    public ScopeController(ScopeService service, MenuRepository menuRepository) {
        this.service = service;
    }

    @GetMapping("/handbook/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button().setTitle("Добавить")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/scope/add")
                ));
        return new Content().setPageName(PAGE_NAME)
                .setManagement(buttons)
                .setTable(getTableAll());
    }

    @GetMapping("/handbook/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/handbook/scope/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        Scope scope = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/scope/" + scope.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(scope.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(scope.getDescription())
                )));
    }

    @PutMapping("/handbook/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdScopeDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody ScopeDto scopeDto) {
        if (scopeDto.getName() == null || scopeDto.getName().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Наименование должно быть заполнено")
            ));
        } else {
            service.edit(service.getById(id), scopeDto.getName(), scopeDto.getDescription());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Область доступа (Scope) успешно сохранена")
            ));
        }
    }

    @GetMapping("/handbook/scope/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/scope")
                                )
                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование")
                                        .setName("name"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Описание")
                                        .setName("description")
                        ))
                );
    }

    @PostMapping("/handbook/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackScopeDto")
    public Content create(@RequestBody ScopeDto scopeDto) {
        if (scopeDto.getName() == null || scopeDto.getName().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Наименование должно быть заполнено")
                    ));
        } else {
            Scope scope = service.add(scopeDto.getName(), scopeDto.getDescription());
            return getContentView(scope.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Область доступа (Scope) успешно добавлена")
                    ));
        }
    }

    @DeleteMapping("/handbook/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Scope успешно удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Scope");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(new Button().setTitle("Добавить")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/scope/add")
                        )
                ))
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
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

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Scope scope : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/handbook/scope/" + scope.getId())
                    )
                    .setColumns(List.of(
                            scope.getName(),
                            scope.getDescription() == null ? "" : scope.getDescription()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Нименование", "Описание"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
        Scope scope = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Редактировать")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/scope/edit/" + scope.getId())
                                )
                        , new Button().setTitle("Удалить")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/scope/" + scope.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(scope.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(scope.getDescription())
                ));
    }
}