package ru.rncb.dpec.controller.dp.systems;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.Systems;
import ru.rncb.dpec.dto.in.dp.systems.SystemsDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.PermissionsService;
import ru.rncb.dpec.service.dp.SysPermissionsService;
import ru.rncb.dpec.service.dp.SystemsService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SystemsController {

    private final SystemsService service;
    private final SysPermissionsService sysPermissionsService;
    private final PermissionsService permissionsService;
    private final static String PAGE_NAME = "Сиситемы";

    public SystemsController(SystemsService service, MenuRepository menuRepository,
                             SysPermissionsService sysPermissionsService, PermissionsService permissionsService) {
        this.service = service;
        this.sysPermissionsService = sysPermissionsService;
        this.permissionsService = permissionsService;
        addMenu(menuRepository);
    }

    @GetMapping("/systems")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/add")
                                )
                ))
                .setTable(getTableAll());
    }

    @GetMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/systems/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        Systems systems = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/systems/" + systems.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systems.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(systems.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(systems.getDescription())
                )));
    }

    @PutMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdSystemsDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody SystemsDto systemsDto) {
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

    @GetMapping("/systems/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/systems")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Наименование")
                                        .setName("name"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Описание")
                                        .setName("description")
                        ))
                );
    }

    @PostMapping("/systems")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsDto")
    public Content create(@RequestBody SystemsDto systemsDto) {
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

    @DeleteMapping("/systems/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
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
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems")
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/add")
                                )
                ))
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @DeleteMapping("/systems/{systems_id}/parameter_val/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("systems_id") long systemsId,
                          @PathVariable("id") long id) {
        Notification notification = new Notification();
        if (sysPermissionsService.delete(sysPermissionsService.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Значение URL-параметра удалено из системы " + service.getById(systemsId).getName());
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления URL-параметра");
        }
        return getContentView(systemsId)
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

    private Content fallbackIdSystemsDto(long id, SystemsDto systemsDto) {
        return fallback();
    }

    private Content fallbackSystemsDto(SystemsDto systemsDto) {
        return fallback();
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

    private Content getContentView(long systemId) {
        Systems system = service.getById(systemId);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + system.getId() + "/edit")
                                ),
                        new Button().setTitle("Добавить URL параметр")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + system.getId() + "/url_parameter/add")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(4)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + system.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(system.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Описание")
                                .setName("description")
                                .setValue(system.getDescription())
                ))
                .setTable(getTableParameters(system));
    }

    private Table getTableParameters(Systems systems) {
        List<Row> rows = new ArrayList<>();
        for (SysPermissions sysPermissions : systems.getSysPermissionsList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/systems/" + systems.getId() + "/parameter_val/" + sysPermissions.getId())
                    )
                    .setColumns(List.of(
                            sysPermissions.getComparing(),
                            sysPermissions.getIsDefault() == 1 ? "&check;" : "",
                            String.valueOf(sysPermissions.getExpire()),
                            sysPermissions.getPermissions() == null ? "" : sysPermissions.getPermissions().getMnemonic(),
                            sysPermissions.getResponsibleobject() == null ? "" : sysPermissions.getResponsibleobject()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of(
                        "Значение URL параметра",
                        "Применять по умолчанию",
                        "Время жизни согласия (минут)",
                        "Согласие",
                        "Организация/ФИО ответственного"
                ))
                .setRows(rows);
    }

    private void addMenu(MenuRepository menuRepository) {
        if (menuRepository.findByLink("/systems").size() == 0) {
            Menu menu = new Menu().setTitle(PAGE_NAME)
                    .setPosition(1)
                    .setMethod(HttpMethod.GET.name())
                    .setLink("/systems")
                    .setAlt(true);
            menuRepository.save(menu);
        }
    }
}