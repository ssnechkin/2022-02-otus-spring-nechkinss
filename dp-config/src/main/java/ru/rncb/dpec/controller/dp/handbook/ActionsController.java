package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.domain.dto.in.dp.handbook.ActionsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.dp.handbook.ActionsService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ActionsController {

    private final ActionsService service;
    private final static String PAGE_NAME = "Справочники > Действия запроса согласия";

    public ActionsController(ActionsService service) {
        this.service = service;
    }

    @GetMapping("/handbook/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @GetMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/handbook/actions/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        Actions actions = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/actions/" + actions.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions/" + actions.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(actions.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(actions.getName())
                )));
    }

    @PutMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdActionsDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody ActionsDto actionsDto) {
        if (actionsDto.getMnemonic() == null || actionsDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), actionsDto.getMnemonic(), actionsDto.getName());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Действие согласия успешно сохранено")
            ));
        }
    }

    @GetMapping("/handbook/actions/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/actions")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions")
                                )
                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Мнемоника")
                                        .setName("mnemonic"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование")
                                        .setName("name")
                        ))
                );
    }

    @PostMapping("/handbook/actions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackActionsDto")
    public Content create(@RequestBody ActionsDto actionsDto) {
        if (actionsDto.getMnemonic() == null || actionsDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            Actions actions = service.add(actionsDto.getMnemonic(), actionsDto.getName());
            return getContentView(actions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Действие согласия успешно добавлено")
                    ));
        }
    }

    @DeleteMapping("/handbook/actions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Действие согласия успешно удалено");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления действия согласия");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
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

    private Content fallbackIdActionsDto(long id, ActionsDto actionsDto) {
        return fallback();
    }

    private Content fallbackActionsDto(ActionsDto actionsDto) {
        return fallback();
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Назад")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/")
                        ),
                new Button().setTitle("Добавить запись")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/actions/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Actions actions : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/handbook/actions/" + actions.getId())
                    )
                    .setColumns(List.of(
                            actions.getMnemonic(),
                            actions.getName() == null ? "" : actions.getName()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Наименование"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
        Actions actions = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions/edit/" + actions.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/actions/" + actions.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(actions.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(actions.getName())
                ));
    }
}