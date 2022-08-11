package ru.rncb.dpec.service.ui.permissions;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsPurposesDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.service.dp.handbook.PurposesService;
import ru.rncb.dpec.service.dp.permissions.PermissionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionsPurposesUiServiceImpl implements PermissionsPurposesUiService{

    private final PermissionsService service;
    private final PurposesService purposesService;
    private final static String PAGE_NAME = "Согласия > ";
    private final static String PAGE_NAME_PURPOSES = " > Цели";

    public PermissionsPurposesUiServiceImpl(PermissionsService service, PurposesService purposesService) {
        this.service = service;
        this.purposesService = purposesService;
    }

    @Override
    public Content list(long permissionsId) {
        return new Content().setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_PURPOSES)
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
        List<Purposes> purposesListList = null;
        if (permissions != null) {
            purposesListList = permissions.getPurposesList();
        }
        if (purposesListList == null) purposesListList = new ArrayList<>();
        List<Purposes> finalPurposesList = purposesListList;
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_PURPOSES + " - Добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions/" + permissionsId + "/purposes")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/purposes")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Цель")
                                        .setName("purposes")
                                        .setValues(purposesService.getAll()
                                                .stream()
                                                .filter(purposes -> !finalPurposesList.contains(purposes))
                                                .map(this::toValueItem)
                                                .toList())
                        ))
                );
    }

    @Override
    public Content create(long permissionsId, PermissionsPurposesDto permissionsPurposesDto) {
        Permissions permissions = service.getById(permissionsId);
        String notification;
        if (permissions != null && service.addPurposes(permissions, purposesService.getById(permissionsPurposesDto.getPurposes()))) {
            notification = "Цель добавлена в Согласие " + permissions.getMnemonic();
        } else if (permissions == null) {
            notification = "Ошибка добавления. Согласии не найдено";
        } else {
            notification = "Ошибка добавления. Цель не найден или уже есть в согласии " + permissions.getMnemonic();
        }
        return getContentView(permissionsId, permissionsPurposesDto.getPurposes())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage(notification)
                ));
    }

    @Override
    public Content delete(long permissionsId, long id) {
        Notification notification = new Notification();
        Permissions permissions = service.getById(permissionsId);
        Purposes purposes = purposesService.getById(id);
        if (permissions != null) {
            if (purposes != null) {
                if (service.deletePurposes(permissions, purposes)) {
                    notification.setType(NotificationType.INFO);
                    notification.setMessage("Цель уделена из Согласия " + permissions.getMnemonic());
                } else {
                    notification.setType(NotificationType.WARNING);
                    notification.setMessage("Ошибка удаления Цели из Согласия " + permissions.getMnemonic()
                            + " Цель " + purposes.getMnemonic() + " отсутствует в согласии");
                }
            } else {
                notification.setType(NotificationType.INFO);
                notification.setMessage("Ошибка удаления Цели из Согласия. Цель не найден");
            }
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Цели из Согласия. Согласие не найдено");
        }
        return new Content()
                .setPageName(PAGE_NAME + PAGE_NAME_PURPOSES)
                .setManagement(getBaseManagement(permissionsId))
                .setTable(getTableAll(permissionsId))
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long permissionsId, long id) {
        Purposes purposes = purposesService.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + service.getById(permissionsId).getMnemonic() + PAGE_NAME_PURPOSES)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/purposes")
                                ),
                        new Button().setTitle("Удалить из согласия")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissionsId + "/purposes/" + id)
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

    private List<Button> getBaseManagement(long permissionsId) {
        return List.of(
                new Button().setTitle("Назад")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId)
                        ),
                new Button().setTitle("Добавить запись")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/permissions/" + permissionsId + "/purposes/add")
                        )
        );
    }

    private ValueItem toValueItem(Purposes purposes) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(purposes.getId());
        valueItem.setValue(purposes.getMnemonic());
        return valueItem;
    }

    private Table getTableAll(long permissionsId) {
        List<Row> rows = new ArrayList<>();
        for (Purposes purposes : service.getById(permissionsId).getPurposesList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/permissions/" + permissionsId + "/purposes/" + purposes.getId())
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