package ru.rncb.dpec.service.ui.handbook;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.handbook.PurposesDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.Color;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.service.dp.handbook.PurposesService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurposesUiServiceImpl implements PurposesUiService {

    private final PurposesService service;
    private final static String PAGE_NAME = "Справочники > Цели запроса согласия";

    public PurposesUiServiceImpl(PurposesService service) {
        this.service = service;
    }

    @Override
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @Override
    public Content view(long id) {
        return getContentView(id);
    }

    @Override
    public Content edit(long id) {
        Purposes purposes = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/purposes/" + purposes.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content save(long id, PurposesDto purposesDto) {
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

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/purposes")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content create(PurposesDto purposesDto) {
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

    @Override
    public Content delete(long id) {
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

    @Override
    public Content getContentView(long id) {
        Purposes purposes = service.getById(id);
        if (purposes == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Цель отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/purposes/edit/" + purposes.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/purposes/" + purposes.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(purposes.getMnemonic()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(purposes.getName())
                ));
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Назад")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/")
                        ),
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
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
}