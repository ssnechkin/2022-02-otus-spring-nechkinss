package ru.dpec.service.ui.systems;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.in.dp.systems.SystemsDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.*;
import ru.dpec.domain.dto.out.enums.Color;
import ru.dpec.domain.dto.out.enums.FieldType;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.domain.entity.dp.Systems;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.dto.out.content.table.Table;
import ru.dpec.domain.entity.dp.SysPermissions;
import ru.dpec.service.dp.systems.SysPermissionsService;
import ru.dpec.service.dp.systems.SystemsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemsUiServiceImpl implements SystemsUiService {

    private final SystemsService service;
    private final SysPermissionsService sysPermissionsService;
    private final static String PAGE_NAME = "Системы";

    public SystemsUiServiceImpl(SystemsService service, SysPermissionsService sysPermissionsService) {
        this.service = service;
        this.sysPermissionsService = sysPermissionsService;
    }

    @Override
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getManagement())
                .setTable(getTableAll());
    }

    @Override
    public Content view(long id) {
        return getContentView(id);
    }

    @Override
    public Content edit(long id) {
        Systems systems = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/systems/" + systems.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systems.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(systems.getName()),
                        new Field().setType(FieldType.TEXTAREA)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(systems.getDescription())
                )));
    }

    @Override
    public Content save(long id, SystemsDto systemsDto) {
        if (systemsDto.getName() == null || systemsDto.getName().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Наименование должно быть заполнено")
            ));
        } else {
            service.edit(service.getById(id), systemsDto.getName(), systemsDto.getDescription());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Система успешно сохранена")
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
                                        .setValue("/systems")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems")
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
    public Content create(SystemsDto systemsDto) {
        if (systemsDto.getName() == null || systemsDto.getName().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Наименование должно быть заполнено")
                    ));
        } else {
            Systems systems = service.add(systemsDto.getName(), systemsDto.getDescription());
            return getContentView(systems.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Система успешно добавлена")
                    ));
        }
    }

    @Override
    public Content delete(long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Система успешно удалена");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Системы");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(getManagement())
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @Override
    public Content delete(long systemsId, long id) {
        Notification notification = new Notification();
        if (sysPermissionsService.delete(sysPermissionsService.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("URL-параметр удален из системы " + service.getById(systemsId).getName());
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления URL-параметра");
        }
        return getContentView(systemsId)
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long systemId) {
        Systems system = service.getById(systemId);
        if (system == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Система отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + system.getId() + "/edit")
                                ),
                        new Button().setTitle("Добавить URL параметр")
                                .setColor(Color.cyan)
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + system.getId() + "/url_parameter/add")
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(4)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + system.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(system.getName()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(system.getDescription())
                ))
                .setTable(getTableParameters(system));
    }

    private List<Button> getManagement() {
        return List.of(
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/systems/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Systems systems : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/systems/" + systems.getId())
                    )
                    .setColumns(List.of(
                            systems.getName() == null ? "" : systems.getName(),
                            systems.getDescription() == null ? "" : systems.getDescription()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Нименование", "Описание"))
                .setRows(rows);
    }

    private Table getTableParameters(Systems systems) {
        List<Row> rows = new ArrayList<>();
        for (SysPermissions sysPermissions : systems.getSysPermissionsList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/systems/" + systems.getId() + "/parameter_val/" + sysPermissions.getId())
                    )
                    .setColumns(List.of(
                            sysPermissions.getResponsibleobject() == null ? "" : sysPermissions.getResponsibleobject(),
                            sysPermissions.getComparing() == null ? "" : sysPermissions.getComparing(),
                            sysPermissions.getIsDefault() == 1 ? "&check;" : "",
                            sysPermissions.getPermissions() == null && sysPermissions.getPermissions() != null ? ""
                                    : sysPermissions.getPermissions().getMnemonic(),
                            sysPermissions.getPermissions() == null && sysPermissions.getPermissions() != null ? ""
                                    : sysPermissions.getPermissions().getResponsibleobject(),
                            String.valueOf(sysPermissions.getExpire())
                    ))
            );
        }
        return new Table()
                .setLabels(List.of(
                        "Наименование URL параметра",
                        "Значение URL параметра",
                        "Применять по умолчанию",
                        "Согласие",
                        "Организация/ФИО ответственного",
                        "Время жизни согласия (минут)"
                ))
                .setRows(rows);
    }
}