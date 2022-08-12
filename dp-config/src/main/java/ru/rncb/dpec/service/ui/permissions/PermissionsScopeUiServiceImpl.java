package ru.rncb.dpec.service.ui.permissions;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsScopeDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.Color;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.service.dp.handbook.ScopeService;
import ru.rncb.dpec.service.dp.permissions.PermissionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionsScopeUiServiceImpl implements PermissionsScopeUiService {

    private final PermissionsService service;
    private final ScopeService scopeService;
    private final static String PAGE_NAME = "Согласия > ";
    private final static String PAGE_NAME_SCOPE = " > Области доступа (Scope)";

    public PermissionsScopeUiServiceImpl(PermissionsService service, ScopeService scopeService) {
        this.service = service;
        this.scopeService = scopeService;
    }

    @Override
    public Content list(long permissionsId) {
        return new Content().setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_SCOPE)
                .setManagement(getBaseManagement(permissionsId))
                .setTable(getTableAll(permissionsId));
    }

    @Override
    public Content view(long permissionsId, long id) {
        return getContentView(permissionsId, id);
    }

    @Override
    public Content add(long permissionsId) {
        Permissions permissions = service.getById(permissionsId);
        List<Scope> scopeList = null;
        if (permissions != null) {
            scopeList = permissions.getScopeList();
        }
        if (scopeList == null) scopeList = new ArrayList<>();
        List<Scope> finalScopeList = scopeList;
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_SCOPE + " - Добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions/" + permissionsId + "/scope")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/scope")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Область доступа (Scope)")
                                        .setName("scope")
                                        .setValues(scopeService.getAll()
                                                .stream()
                                                .filter(scope -> !finalScopeList.contains(scope))
                                                .map(this::toValueItem)
                                                .toList())
                        ))
                );
    }

    @Override
    public Content create(long permissionsId, PermissionsScopeDto permissionsScopeDto) {
        Permissions permissions = service.getById(permissionsId);
        String notification;
        if (permissions != null && service.addScope(permissions, scopeService.getById(permissionsScopeDto.getScope()))) {
            notification = "Scope добавлен в Согласие " + permissions.getMnemonic();
        } else if (permissions == null) {
            notification = "Ошибка добавления. Согласие не найдено";
        } else {
            notification = "Ошибка добавления. Scope не найден или уже есть в согласии " + permissions.getMnemonic();
        }
        return getContentView(permissionsId, permissionsScopeDto.getScope())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage(notification)
                ));
    }

    @Override
    public Content delete(long permissionsId, long id) {
        Notification notification = new Notification();
        Permissions permissions = service.getById(permissionsId);
        Scope scope = scopeService.getById(id);
        if (permissions != null) {
            if (scope != null) {
                if (service.deleteScope(permissions, scope)) {
                    notification.setType(NotificationType.INFO);
                    notification.setMessage("Scope уделен из Согласия " + permissions.getMnemonic());
                } else {
                    notification.setType(NotificationType.WARNING);
                    notification.setMessage("Ошибка удаления Scope из Согласия " + permissions.getMnemonic()
                            + " Scope " + scope.getName() + " отсутствует в согласии");
                }
            } else {
                notification.setType(NotificationType.INFO);
                notification.setMessage("Ошибка удаления scope из Согласия. Scope не найден");
            }
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления scope из Согласия. Согласие не найдено");
        }
        return new Content()
                .setPageName(PAGE_NAME + PAGE_NAME_SCOPE)
                .setManagement(getBaseManagement(permissionsId))
                .setTable(getTableAll(permissionsId))
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long permissionsId, long id) {
        Scope scope = scopeService.getById(id);
        if (scope == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Scope отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_SCOPE)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/scope")
                                ),
                        new Button().setTitle("Удалить из согласия")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissionsId + "/scope/" + id)
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

    private List<Button> getBaseManagement(long permissionsId) {
        return List.of(
                new Button().setTitle("Назад")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId)
                        ),
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId + "/scope/add")
                        )
        );
    }

    private ValueItem toValueItem(Scope scope) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(scope.getId());
        valueItem.setValue(scope.getName() + " (" + scope.getDescription() + ")");
        return valueItem;
    }

    private Table getTableAll(long permissionsId) {
        List<Row> rows = new ArrayList<>();
        for (Scope scope : service.getById(permissionsId).getScopeList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/permissions/" + permissionsId + "/scope/" + scope.getId())
                    )
                    .setColumns(List.of(
                            scope.getName() == null ? "" : scope.getName(),
                            scope.getDescription() == null ? "" : scope.getDescription()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Нименование", "Описание"))
                .setRows(rows);
    }
}