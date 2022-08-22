package ru.dpec.service.ui.permissions;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.in.dp.permissions.PermissionsDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.*;
import ru.dpec.domain.dto.out.enums.Color;
import ru.dpec.domain.dto.out.enums.FieldType;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.entity.dp.Permissions;
import ru.dpec.domain.entity.dp.handbook.Actions;
import ru.dpec.domain.entity.dp.handbook.Purposes;
import ru.dpec.domain.entity.dp.handbook.Scope;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.dto.out.content.table.Table;
import ru.dpec.service.dp.permissions.PermissionsService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionsUiServiceImpl implements PermissionsUiService {

    private final PermissionsService service;
    private final static String PAGE_NAME = "Согласия";

    public PermissionsUiServiceImpl(PermissionsService service) {
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
        Permissions permissions = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/permissions/" + permissions.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(permissions.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(permissions.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Организация/ФИО ответственного (Оторажается в согласии у клиента в ЕСИА)")
                                .setName("responsibleobject")
                                .setValue(permissions.getResponsibleobject()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Время жизни согласия (минут)")
                                .setName("permission_expire")
                                .setValue(String.valueOf(permissions.getExpire())),
                        new Field().setType(FieldType.TEXTAREA)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(permissions.getDescription())
                )));
    }

    @Override
    public Content save(long id, PermissionsDto permissionsDto) {
        if (permissionsDto.getMnemonic() == null || permissionsDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), permissionsDto.getMnemonic(), permissionsDto.getName(),
                    permissionsDto.getResponsibleobject(), permissionsDto.getPermissionExpire(),
                    permissionsDto.getDescription());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Согласие успешно сохранено")
            ));
        }
    }

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Мнемоника")
                                        .setName("mnemonic"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование")
                                        .setName("name"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Организация/ФИО ответственного " +
                                                "(Оторажается в согласии у клиента в ЕСИА)")
                                        .setName("responsibleobject"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Время жизни согласия (минут)")
                                        .setName("permission_expire"),
                                new Field().setType(FieldType.TEXTAREA)
                                        .setLabel("Описание")
                                        .setName("description")
                        ))
                );
    }

    @Override
    public Content create(PermissionsDto permissionsDto) {
        if (permissionsDto.getMnemonic() == null || permissionsDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            Permissions permissions = service.add(permissionsDto.getMnemonic(), permissionsDto.getName(),
                    permissionsDto.getResponsibleobject(), permissionsDto.getPermissionExpire(),
                    permissionsDto.getDescription());
            return getContentView(permissions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Согласие успешно добавлено")
                    ));
        }
    }

    @Override
    public Content delete(long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Согласие успешно удалено");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Согласия.");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long id) {
        Permissions permissions = service.getById(id);
        if (permissions == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Согласие отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/edit")
                                ),
                        new Button().setTitle("Цели")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/purposes")
                                ),
                        new Button().setTitle("Действия")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/actions")
                                ),
                        new Button().setTitle("Области доступа (Scope)")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/scope")
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissions.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(permissions.getMnemonic()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(permissions.getName()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Организация/ФИО ответственного (Оторажается в согласии у клиента в ЕСИА)")
                                .setName("responsibleobject")
                                .setValue(permissions.getResponsibleobject()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Время жизни согласия (минут)")
                                .setName("permission_expire")
                                .setValue(String.valueOf(permissions.getExpire())),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(permissions.getDescription()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Цели")
                                .setName("purposes")
                                .setValue(permissions.getPurposesList() == null ? ""
                                        : permissions.getPurposesList().stream()
                                        .map(Purposes::getMnemonic).collect(Collectors.joining(", "))),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Действия")
                                .setName("actions")
                                .setValue(permissions.getActionsList() == null ? ""
                                        : permissions.getActionsList().stream()
                                        .map(Actions::getMnemonic).collect(Collectors.joining(", "))),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Области доступа (Scope)")
                                .setName("scope")
                                .setValue(permissions.getScopeList() == null ? ""
                                        : permissions.getScopeList().stream()
                                        .map(Scope::getName).collect(Collectors.joining(", ")))
                ));
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Permissions permissions : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/permissions/" + permissions.getId())
                    )
                    .setColumns(List.of(
                            permissions.getMnemonic(),
                            permissions.getName() == null ? "" : permissions.getName(),
                            permissions.getResponsibleobject() == null ? "" : permissions.getResponsibleobject(),
                            String.valueOf(permissions.getExpire()),
                            permissions.getDescription() == null ? "" : permissions.getDescription(),
                            permissions.getPurposesList() == null ? ""
                                    : permissions.getPurposesList().stream()
                                    .map(Purposes::getMnemonic).collect(Collectors.joining(", ")),
                            permissions.getActionsList() == null ? ""
                                    : permissions.getActionsList().stream()
                                    .map(Actions::getMnemonic).collect(Collectors.joining(", "))
                    ))
            );
        }
        return new Table()
                .setLabels(List.of(
                        "Мнемоника",
                        "Нименование",
                        "Организация/ФИО ответственного",
                        "Время жизни согласия (минут)",
                        "Описание",
                        "Цели",
                        "Действия"
                ))
                .setRows(rows);
    }
}