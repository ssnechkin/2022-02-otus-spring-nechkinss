package ru.dpec.service.ui.handbook;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.*;
import ru.dpec.domain.dto.out.enums.FieldType;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.entity.dp.handbook.Scope;
import ru.dpec.domain.dto.in.dp.handbook.ScopeDto;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.dto.out.content.table.Table;
import ru.dpec.domain.dto.out.enums.Color;
import ru.dpec.service.dp.handbook.ScopeService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScopeUiServiceImpl implements ScopeUiService {

    private final ScopeService service;
    private final static String PAGE_NAME = "Справочники > Области доступа (Scope)";

    public ScopeUiServiceImpl(ScopeService service) {
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
        Scope scope = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/scope/" + scope.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/scope/" + scope.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(scope.getName()),
                        new Field().setType(FieldType.TEXTAREA)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(scope.getDescription())
                )));
    }

    @Override
    public Content save(long id, ScopeDto scopeDto) {
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

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/scope")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/scope")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование")
                                        .setName("name"),
                                new Field().setType(FieldType.TEXTAREA)
                                        .setLabel("Описание")
                                        .setName("description")
                        ))
                );
    }

    @Override
    public Content create(ScopeDto scopeDto) {
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

    @Override
    public Content delete(long id) {
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
                .setManagement(getBaseManagement())
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long id) {
        Scope scope = service.getById(id);
        if (scope == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Scope отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/scope")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/scope/edit/" + scope.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/scope/" + scope.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(scope.getName()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(scope.getDescription())
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
                                .setValue("/handbook/scope/add")
                        )
        );
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
}