package ru.dpec.service.ui.handbook;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.*;
import ru.dpec.domain.dto.out.enums.Color;
import ru.dpec.domain.dto.out.enums.FieldType;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.entity.dp.handbook.Actions;
import ru.dpec.domain.dto.in.dp.handbook.ActionsDto;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.dto.out.content.table.Table;
import ru.dpec.service.dp.handbook.ActionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionsUiServiceImpl implements ActionsUiService {

    private final ActionsService service;
    private final static String PAGE_NAME = "Справочники > Действия запроса согласия";

    public ActionsUiServiceImpl(ActionsService service) {
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
        Actions actions = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/actions/" + actions.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content save(long id, ActionsDto actionsDto) {
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

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/actions")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content create(ActionsDto actionsDto) {
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

    @Override
    public Content delete(long id) {
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

    @Override
    public Content getContentView(long id) {
        Actions actions = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/actions/edit/" + actions.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/actions/" + actions.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(actions.getMnemonic()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(actions.getName())
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
}