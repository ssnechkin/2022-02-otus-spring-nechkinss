package ru.rncb.dpec.service.ui.permissions;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.permissions.PermissionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionsUiServiceImpl implements PermissionsUiService {

    private final PermissionsService service;
    private final static String PAGE_NAME = "Согласия";

    public PermissionsUiServiceImpl(PermissionsService service, MenuRepository menuRepository) {
        this.service = service;
        addMenu(menuRepository);
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
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/permissions/" + permissions.getId())
                                ),
                        new Button().setTitle("Отмена")
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
            service.edit(service.getById(id), permissionsDto.getMnemonic(), permissionsDto.getName(), permissionsDto.getDescription());
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
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions")
                                ),
                        new Button().setTitle("Отмена")
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
            Permissions permissions = service.add(permissionsDto.getMnemonic(), permissionsDto.getName(), permissionsDto.getDescription());
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
            notification.setMessage("Ошибка удаления Согласия");
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
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/edit")
                                ),
                        new Button().setTitle("Цели")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/purposes")
                                ),
                        new Button().setTitle("Действия")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/actions")
                                ),
                        new Button().setTitle("Области доступа (Scope)")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissions.getId() + "/scope")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissions.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(permissions.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(permissions.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(permissions.getDescription()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Цели")
                                .setName("purposes")
                                .setValue(permissions.getPurposesList() == null ? "" : String.join(", ", permissions.getPurposesList().stream().map(Purposes::getMnemonic).toList())),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Действия")
                                .setName("actions")
                                .setValue(permissions.getActionsList() == null ? "" : String.join(", ", permissions.getActionsList().stream().map(Actions::getMnemonic).toList())),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Области доступа (Scope)")
                                .setName("scope")
                                .setValue(permissions.getScopeList() == null ? "" : String.join(", ", permissions.getScopeList().stream().map(Scope::getName).toList()))
                ));
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Добавить запись")
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
                            permissions.getDescription() == null ? "" : permissions.getDescription(),
                            permissions.getPurposesList() == null ? "" : String.join(", ", permissions.getPurposesList().stream().map(Purposes::getMnemonic).toList()),
                            permissions.getActionsList() == null ? "" : String.join(", ", permissions.getActionsList().stream().map(Actions::getMnemonic).toList()),
                            permissions.getScopeList() == null ? "" : String.join(", ", permissions.getScopeList().stream().map(Scope::getName).toList())
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Нименование", "Описание", "Цели", "Действия", "Области доступа (Scope)"))
                .setRows(rows);
    }

    private void addMenu(MenuRepository menuRepository) {
        if (menuRepository.findByLink("/permissions").size() == 0) {
            Menu menu = new Menu().setTitle(PAGE_NAME)
                    .setPosition(2)
                    .setMethod(HttpMethod.GET.name())
                    .setLink("/permissions")
                    .setAlt(true);
            menuRepository.save(menu);
        }
    }
}