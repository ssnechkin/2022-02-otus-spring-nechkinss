package ru.rncb.dpec.controller.dp.permissions;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.dto.in.dp.PermissionsDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.PermissionsService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PermissionsController {

    private final PermissionsService service;
    private final static String PAGE_NAME = "Согласия";

    public PermissionsController(PermissionsService service, MenuRepository menuRepository) {
        this.service = service;
        addMenu(menuRepository);
    }

    @GetMapping("/permissions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/add")
                                )
                ))
                .setTable(getTableAll());
    }

    @GetMapping("/permissions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/permissions/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
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
                                .setValue(permissions.getDescription()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Время жизни согласия (мин)")
                                .setName("expire")
                                .setValue(String.valueOf(permissions.getExpire())),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование организации или ФИО ответственного")
                                .setName("responsibleobject")
                                .setValue(permissions.getResponsibleobject())
                )));
    }

    @PutMapping("/permissions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdPermissionsDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody PermissionsDto permissionsDto) {
        if (permissionsDto.getMnemonic() == null || permissionsDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), permissionsDto.getMnemonic(), permissionsDto.getName(), permissionsDto.getDescription(), permissionsDto.getExpire(), permissionsDto.getResponsibleobject());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Согласие успешно сохранено")
            ));
        }
    }

    @GetMapping("/permissions/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
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
                                        .setName("description"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Время жизни согласия")
                                        .setName("expire"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование организации или ФИО ответственного")
                                        .setName("responsibleobject")
                        ))
                );
    }

    @PostMapping("/permissions")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackPermissionsDto")
    public Content create(@RequestBody PermissionsDto permissionsDto) {
        if (permissionsDto.getMnemonic() == null || permissionsDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            Permissions permissions = service.add(permissionsDto.getMnemonic(), permissionsDto.getName(), permissionsDto.getDescription(), permissionsDto.getExpire(), permissionsDto.getResponsibleobject());
            return getContentView(permissions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Согласие успешно добавлено")
                    ));
        }
    }

    @DeleteMapping("/permissions/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
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
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions")
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/permissions/add")
                                )
                ))
                .setTable(getTableAll())
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
                            String.valueOf(permissions.getExpire()),
                            permissions.getResponsibleobject() == null ? "" : permissions.getResponsibleobject(),
                            permissions.getScopeList() == null ? "" : String.join(", ", permissions.getScopeList().stream().map(Scope::getName).toList())
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Нименование", "Описание", "Время жизни (минуты)", "Организация/ФИО ответственного", "Области доступа (Scope)"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
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
                                .setLabel("Время жизни (минуты)")
                                .setName("expire")
                                .setValue(String.valueOf(permissions.getExpire())),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Организация/ФИО ответственного")
                                .setName("responsibleobject")
                                .setValue(permissions.getResponsibleobject()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Области доступа (Scope)")
                                .setName("scope")
                                .setValue(permissions.getScopeList() == null ? "" : String.join(", ", permissions.getScopeList().stream().map(Scope::getName).toList()))
                ));
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