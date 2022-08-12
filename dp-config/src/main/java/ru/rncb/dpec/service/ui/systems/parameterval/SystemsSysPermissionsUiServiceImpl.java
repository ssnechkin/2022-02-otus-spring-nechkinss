package ru.rncb.dpec.service.ui.systems.parameterval;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.SystemsUrlParameterValDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.Permissions;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;
import ru.rncb.dpec.domain.entity.dp.Systems;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.service.dp.permissions.PermissionsService;
import ru.rncb.dpec.service.dp.systems.SysPermissionsService;
import ru.rncb.dpec.service.dp.systems.SystemsService;

import java.util.List;

@Service
public class SystemsSysPermissionsUiServiceImpl implements SystemsSysPermissionsUiService {

    private final SystemsService service;
    private final SysPermissionsService sysPermissionsService;
    private final PermissionsService permissionsService;
    private final static String PAGE_NAME = "Системы > ";

    public SystemsSysPermissionsUiServiceImpl(SystemsService service, MenuRepository menuRepository,
                                              SysPermissionsService sysPermissionsService, PermissionsService permissionsService) {
        this.service = service;
        this.sysPermissionsService = sysPermissionsService;
        this.permissionsService = permissionsService;
    }

    @Override
    public Content addUrlParameterVal(long systemsId) {
        return new Content()
                .setPageName(PAGE_NAME + getSystemName(systemsId) + " - добавление URL-параметра")
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
                                        .setLabel("Наименование URL параметра")
                                        .setName("url_parameter_name"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Значение URL параметра")
                                        .setName("url_parameter_value"),
                                new Field().setType(FieldType.CHECKBOX)
                                        .setLabel("Применять по умолчанию")
                                        .setName("is_default"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Время жизни согласия (минут)")
                                        .setName("permission_expire"),
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

    @Override
    public Content view(long systemsId, long id) {
        return getContentView(systemsId, id);
    }

    @Override
    public Content createParamVal(long systemsId, SystemsUrlParameterValDto urlParameterValDto) {
        if (urlParameterValDto.getUrlParameterValue() == null || urlParameterValDto.getUrlParameterValue().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Значение URL-параметра должно быть заполнено")
                    ));
        } else if (urlParameterValDto.getUrlParameterName() == null || urlParameterValDto.getUrlParameterName().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Наименование URL-параметра должно быть заполнено")
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
                    urlParameterValDto.getUrlParameterName(),
                    urlParameterValDto.getUrlParameterValue(),
                    urlParameterValDto.getPermissionExpire(),
                    urlParameterValDto.isDefault()
            );
            return getContentView(systemsId, sysPermissions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Запись успешно добавлена")
                    ));
        }
    }

    @Override
    public Content edit(long systemsId, long id) {
        SysPermissions sysPermissions = sysPermissionsService.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + getSystemName(systemsId) + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + id)
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + id)
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование URL параметра")
                                .setName("url_parameter_name")
                                .setValue(sysPermissions.getResponsibleobject()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Значение URL параметра")
                                .setName("url_parameter_value")
                                .setValue(sysPermissions.getComparing()),
                        new Field().setType(FieldType.CHECKBOX)
                                .setLabel("Применять по умолчанию")
                                .setName("is_default")
                                .setChecked(sysPermissions.getIsDefault() == 1),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Время жизни согласия (минут)")
                                .setName("permission_expire")
                                .setValue(String.valueOf(sysPermissions.getExpire())),
                        new Field().setType(FieldType.SELECT)
                                .setLabel("Запрашиваемое согласие")
                                .setName("permissions_id")
                                .setSelectedId(sysPermissions.getPermissions().getId())
                                .setValues(permissionsService.getAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .toList())
                )));
    }

    @Override
    public Content save(long systemsId, long id, SystemsUrlParameterValDto urlParameterValDto) {
        if (urlParameterValDto.getUrlParameterValue() == null || urlParameterValDto.getUrlParameterValue().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Значение URL-параметра должно быть заполнено")
                    ));
        } else if (urlParameterValDto.getUrlParameterName() == null || urlParameterValDto.getUrlParameterName().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Наименование URL-параметра должно быть заполнено")
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
            SysPermissions sysPermissions = sysPermissionsService.edit(
                    sysPermissionsService.getById(id),
                    service.getById(systemsId),
                    permissionsService.getById(urlParameterValDto.getPermissionId()),
                    urlParameterValDto.getUrlParameterName(),
                    urlParameterValDto.getUrlParameterValue(),
                    urlParameterValDto.getPermissionExpire(),
                    urlParameterValDto.isDefault()
            );
            return getContentView(systemsId, sysPermissions.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Запись успешно изменена")
                    ));
        }
    }

    @Override
    public Content getContentView(long systemsId, long sysPermissionsId) {
        SysPermissions sysPermissions = sysPermissionsService.getById(sysPermissionsId);
        if (sysPermissions == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Значение параметра отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME + getSystemName(systemsId))
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
                        new Button().setTitle("Список запрашиваемых документов")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + sysPermissionsId + "/requested_documents")
                                ),
                        new Button().setTitle("Фильтр ответа")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + sysPermissionsId + "/response_filter")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(4)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + systemsId + "/parameter_val/" + sysPermissionsId)
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование URL параметра")
                                .setName("url_parameter_name")
                                .setValue(sysPermissions.getResponsibleobject()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Значение URL параметра")
                                .setName("url_parameter_value")
                                .setValue(sysPermissions.getComparing()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Применять по умолчанию")
                                .setName("is_default")
                                .setValue(sysPermissions.getIsDefault() == 1 ? "&check;" : ""),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Время жизни согласия (минут)")
                                .setName("permission_expire")
                                .setValue(String.valueOf(sysPermissions.getExpire())),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Запрашиваемое согласие")
                                .setName("permissions_id")
                                .setValue(sysPermissions.getPermissions().getMnemonic())
                ));
    }

    private ValueItem toValueItem(Permissions permissions) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(permissions.getId());
        valueItem.setValue(permissions.getMnemonic() + " (" + permissions.getDescription() + ")");
        return valueItem;
    }

    private String getSystemName(long systemId) {
        Systems systems = service.getById(systemId);
        if (systems != null) {
            return systems.getName();
        }
        return "";
    }
}