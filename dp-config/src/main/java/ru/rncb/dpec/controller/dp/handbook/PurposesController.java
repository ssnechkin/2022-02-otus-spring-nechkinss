package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.domain.dto.in.dp.handbook.PurposesDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.dp.handbook.PurposesService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PurposesController {

    private final PurposesService service;
    private final static String PAGE_NAME = "Справочники > Цели запроса согласия";

    public PurposesController(PurposesService service) {
        this.service = service;
    }

    @GetMapping("/handbook/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @GetMapping("/handbook/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/handbook/purposes/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        Purposes purposes = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/purposes/" + purposes.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes/" + purposes.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(purposes.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(purposes.getName())
                )));
    }

    @PutMapping("/handbook/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdPurposesDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody PurposesDto purposesDto) {
        if (purposesDto.getMnemonic() == null || purposesDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), purposesDto.getMnemonic(), purposesDto.getName());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Цель согласия успешно сохранено")
            ));
        }
    }

    @GetMapping("/handbook/purposes/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/purposes")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes")
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

    @PostMapping("/handbook/purposes")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPurposesDto")
    public Content create(@RequestBody PurposesDto purposesDto) {
        if (purposesDto.getMnemonic() == null || purposesDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            Purposes purposes = service.add(purposesDto.getMnemonic(), purposesDto.getName());
            return getContentView(purposes.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Цель согласия успешно добавлено")
                    ));
        }
    }

    @DeleteMapping("/handbook/purposes/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Цель согласия успешно удалено");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления цели согласия");
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

    private Content fallbackIdPurposesDto(long id, PurposesDto purposesDto) {
        return fallback();
    }

    private Content fallbackPurposesDto(PurposesDto purposesDto) {
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
                                .setValue("/handbook/purposes/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Purposes purposes : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/handbook/purposes/" + purposes.getId())
                    )
                    .setColumns(List.of(
                            purposes.getMnemonic(),
                            purposes.getName() == null ? "" : purposes.getName()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Наименование"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
        Purposes purposes = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes/edit/" + purposes.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/purposes/" + purposes.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(purposes.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(purposes.getName())
                ));
    }
}