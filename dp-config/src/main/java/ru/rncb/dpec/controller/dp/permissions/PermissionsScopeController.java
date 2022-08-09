package ru.rncb.dpec.controller.dp.permissions;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.dto.in.dp.PermissionsDto;
import ru.rncb.dpec.dto.in.dp.PermissionsScopeDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.PermissionsService;
import ru.rncb.dpec.service.dp.handbook.ScopeService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PermissionsScopeController {

    private final PermissionsService service;
    private final ScopeService scopeService;
    private final static String PAGE_NAME = "Согласия - Области доступа (Scope)";

    public PermissionsScopeController(PermissionsService service, MenuRepository menuRepository, ScopeService scopeService) {
        this.service = service;
        this.scopeService = scopeService;
    }

    @GetMapping("/permissions/{permissions_id}/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list(@PathVariable("permissions_id") long permissionsId) {
        return new Content().setPageName(PAGE_NAME + " " + service.getById(permissionsId).getMnemonic())
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId)
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/scope/add")
                                )
                ))
                .setTable(getTableAll(permissionsId));
    }

    @GetMapping("/permissions/{permissions_id}/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("permissions_id") long permissionsId, @PathVariable("id") long id) {
        return getContentView(permissionsId, id);
    }

    @GetMapping("/permissions/{permissions_id}/scope/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add(@PathVariable("permissions_id") long permissionsId) {
        Permissions permissions = service.getById(permissionsId);
        List<Scope> scopeList = null;
        if (permissions != null) {
            scopeList = permissions.getScopeList();
        }
        if (scopeList == null) scopeList = new ArrayList<>();
        List<Scope> finalScopeList = scopeList;
        return new Content()
                .setPageName(PAGE_NAME + " " + service.getById(permissionsId).getMnemonic() + " - добавление Scope")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/permissions/" + permissionsId + "/scope")
                                ),
                        new Button().setTitle("Отмена")
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

    @PostMapping("/permissions/{permissions_id}/scope")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsDto")
    public Content create(@PathVariable("permissions_id") long permissionsId, @RequestBody PermissionsScopeDto permissionsScopeDto) {
        Permissions permissions = service.getById(permissionsId);
        String notification;
        if (permissions != null && service.addScope(permissions, permissionsScopeDto.getScope())) {
            notification = "Scope добавлен в Согласие " + permissions.getMnemonic();
        } else {
            notification = "Ошибка добавления. Scope не найден или уже есть в согласии " + permissions.getMnemonic();
        }
        return getContentView(permissionsId, permissionsScopeDto.getScope())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage(notification)
                ));
    }

    @DeleteMapping("/permissions/{permissions_id}/scope/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("permissions_id") long permissionsId, @PathVariable("id") long id) {
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
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId)
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/scope/add")
                                )
                ))
                .setTable(getTableAll(permissionsId))
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

    private Content fallbackIdPermissionsDto(long id, PermissionsDto permissionsDto) {
        return fallback();
    }

    private Content fallbackPermissionsDto(PermissionsDto permissionsDto) {
        return fallback();
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

    private Content getContentView(long permissionsId, long id) {
        Scope scope = scopeService.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " " + service.getById(permissionsId).getMnemonic())
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/" + permissionsId + "/scope")
                                ),
                        new Button().setTitle("Удалить из согласия")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/permissions/" + permissionsId + "/scope/" + id)
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(scope.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(scope.getDescription())
                ));
    }
}