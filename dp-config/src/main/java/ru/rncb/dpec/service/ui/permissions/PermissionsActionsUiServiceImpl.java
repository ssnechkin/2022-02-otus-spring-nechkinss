package ru.rncb.dpec.service.ui.permissions;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsActionsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.service.dp.handbook.ActionsService;
import ru.rncb.dpec.service.dp.permissions.PermissionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionsActionsUiServiceImpl implements PermissionsActionsUiService {

    private final PermissionsService service;
    private final ActionsService actionsService;
    private final static String PAGE_NAME = "Согласия > ";
    private final static String PAGE_NAME_ACTIONS = " > Действия";

    public PermissionsActionsUiServiceImpl(PermissionsService service, ActionsService actionsService) {
        this.service = service;
        this.actionsService = actionsService;
    }

    @Override
    public Content list(long permissionsId) {
        return new Content().setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_ACTIONS)
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
        List<Actions> actionsList = null;
        if (permissions != null) {
            actionsList = permissions.getActionsList();
        }
        if (actionsList == null) actionsList = new ArrayList<>();
        List<Actions> finalActionsList = actionsList;
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_ACTIONS + " - Добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions/" + permissionsId + "/actions")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/actions")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Действие")
                                        .setName("actions")
                                        .setValues(actionsService.getAll()
                                                .stream()
                                                .filter(actions -> !finalActionsList.contains(actions))
                                                .map(this::toValueItem)
                                                .toList())
                        ))
                );
    }

    @Override
    public Content create(long permissionsId, PermissionsActionsDto permissionsActionsDto) {
        Permissions permissions = service.getById(permissionsId);
        String notification;
        if (permissions != null && service.addActions(permissions, actionsService.getById(permissionsActionsDto.getActions()))) {
            notification = "Действие добавлено в Согласие " + permissions.getMnemonic();
        } else if (permissions == null) {
            notification = "Ошибка добавления. Согласии не найдено";
        } else {
            notification = "Ошибка добавления. Действие не найдено или уже есть в согласии " + permissions.getMnemonic();
        }
        return getContentView(permissionsId, permissionsActionsDto.getActions())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage(notification)
                ));
    }

    @Override
    public Content delete(long permissionsId, long id) {
        Notification notification = new Notification();
        Permissions permissions = service.getById(permissionsId);
        Actions actions = actionsService.getById(id);
        if (permissions != null) {
            if (actions != null) {
                if (service.deleteActions(permissions, actions)) {
                    notification.setType(NotificationType.INFO);
                    notification.setMessage("Действие уделено из Согласия " + permissions.getMnemonic());
                } else {
                    notification.setType(NotificationType.WARNING);
                    notification.setMessage("Ошибка удаления Действия из Согласия " + permissions.getMnemonic()
                            + " Действие " + actions.getMnemonic() + " отсутствует в согласии");
                }
            } else {
                notification.setType(NotificationType.INFO);
                notification.setMessage("Ошибка удаления Действия из Согласия. Действие не найдено");
            }
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Действия из Согласия. Согласие не найдено");
        }
        return new Content()
                .setPageName(PAGE_NAME + PAGE_NAME_ACTIONS)
                .setManagement(getBaseManagement(permissionsId))
                .setTable(getTableAll(permissionsId))
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long permissionsId, long id) {
        Actions actions = actionsService.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_ACTIONS)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/actions")
                                ),
                        new Button().setTitle("Удалить из согласия")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissionsId + "/actions/" + id)
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

    private List<Button> getBaseManagement(long permissionsId) {
        return List.of(
                new Button().setTitle("Назад")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId)
                        ),
                new Button().setTitle("Добавить запись")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId + "/actions/add")
                        )
        );
    }

    private ValueItem toValueItem(Actions actions) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(actions.getId());
        valueItem.setValue(actions.getMnemonic());
        return valueItem;
    }

    private Table getTableAll(long permissionsId) {
        List<Row> rows = new ArrayList<>();
        for (Actions actions : service.getById(permissionsId).getActionsList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/permissions/" + permissionsId + "/actions/" + actions.getId())
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