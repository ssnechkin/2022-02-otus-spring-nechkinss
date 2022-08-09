package ru.rncb.dpec.controller.dp.systems;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.dto.in.dp.systems.SystemsDto;
import ru.rncb.dpec.dto.in.dp.systems.SystemsUrlParameterValDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.PermissionsService;
import ru.rncb.dpec.service.dp.SysPermissionsService;
import ru.rncb.dpec.service.dp.SystemsService;

import java.util.List;

@RestController
public class SystemsSysPermissionsController {

    private final SystemsService service;
    private final SysPermissionsService sysPermissionsService;
    private final PermissionsService permissionsService;
    private final static String PAGE_NAME = "Сиситемы";

    public SystemsSysPermissionsController(SystemsService service, MenuRepository menuRepository,
                                           SysPermissionsService sysPermissionsService, PermissionsService permissionsService) {
        this.service = service;
        this.sysPermissionsService = sysPermissionsService;
        this.permissionsService = permissionsService;
    }

    @GetMapping("/systems/{systems_id}/url_parameter/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content addUrlParameterVal(@PathVariable("systems_id") long systemsId) {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление значения URL-параметра")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/systems/" + systemsId + "/parameter_val")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId)
                                )
                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Значение URL параметра")
                                        .setName("url_parameter_value"),
                                new Field().setType(FieldType.CHECKBOX)
                                        .setLabel("Применять по умолчанию")
                                        .setName("is_default"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Время жизни согласия (минут)")
                                        .setName("permission_expire"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Организация/ФИО ответственного")
                                        .setName("responsibleobject"),
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Запрашиваемое согласие")
                                        .setName("permissions_id")
                                        .setValues(permissionsService.getAll()
                                                .stream()
                                                .map(this::toValueItem)
                                                .toList())
                        ))
                );
    }

    @GetMapping("/systems/{systems_id}/parameter_val/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("systems_id") long systemsId,
                        @PathVariable("id") long id) {
        return getContentView(systemsId, id);
    }

    @PostMapping("/systems/{systems_id}/parameter_val")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackSystemsDto")
    public Content createParamVal(@PathVariable("systems_id") long systemsId,
                                  @RequestBody SystemsUrlParameterValDto urlParameterValDto) {
        if (urlParameterValDto.getUrlParameterValue() == null || urlParameterValDto.getUrlParameterValue().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Значение URL-параметра должно быть заполнено")
                    ));
        } else if (service.getById(systemsId) == null) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Система не найдена")
                    ));
        } else if (permissionsService.getById(urlParameterValDto.getPermissionId()) == null) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Согласие не найдено. Согласие должно быть выбрано")
                    ));
        } else {
            SysPermissions sysPermissions = sysPermissionsService.add(service.getById(systemsId),
                    permissionsService.getById(urlParameterValDto.getPermissionId()),
                    urlParameterValDto.getUrlParameterValue(),
                    urlParameterValDto.getResponsibleobject(),
                    urlParameterValDto.getPermissionExpire(),
                    (urlParameterValDto.getIsDefault() != null && urlParameterValDto.getIsDefault().equals("on"))
            );
            return getContentView(systemsId, sysPermissions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Значение URL-параметра успешно добавлено")
                    ));
        }
    }

    private ValueItem toValueItem(Permissions permissions) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(permissions.getId());
        valueItem.setValue(permissions.getMnemonic() + " (" + permissions.getDescription() + ")");
        return valueItem;
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

    private Content getContentView(long systemsId, long sysPermissionsId) {
        SysPermissions sysPermissions = sysPermissionsService.getById(sysPermissionsId);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId)
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + sysPermissionsId + "/edit")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(4)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + sysPermissionsId)
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Значение URL параметра")
                                .setName("url_parameter_value")
                                .setValue(sysPermissions.getComparing()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Применять по умолчанию")
                                .setName("is_default")
                                .setValue(sysPermissions.getIsDefault() == 1 ? "+" : ""),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Время жизни согласия (минут)")
                                .setName("permission_expire")
                                .setValue(String.valueOf(sysPermissions.getExpire())),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Организация/ФИО ответственного")
                                .setName("responsibleobject")
                                .setValue(sysPermissions.getResponsibleobject()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Запрашиваемое согласие")
                                .setName("permissions_id")
                                .setValue(sysPermissions.getPermissions().getMnemonic())
                ));
    }

}