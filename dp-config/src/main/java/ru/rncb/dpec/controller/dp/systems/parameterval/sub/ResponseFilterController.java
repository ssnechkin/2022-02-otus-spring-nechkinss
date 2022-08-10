package ru.rncb.dpec.controller.dp.systems.parameterval.sub;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.dto.in.dp.handbook.ScopeDto;
import ru.rncb.dpec.dto.in.dp.systems.parameterval.sub.ResponseFilerDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.systems.SysPermissionsService;
import ru.rncb.dpec.service.dp.systems.SysResponseService;
import ru.rncb.dpec.service.dp.systems.SystemsService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResponseFilterController {

    private final SysResponseService service;
    private final SystemsService systemsService;
    private final SysPermissionsService sysPermissionsService;
    private final DocumentTypeService documentTypeService;
    private final static String PAGE_NAME = "Системы > ";
    private final static String PAGE_NAME_PAR_VAL = " > Значение параметра: ";
    private final static String PAGE_NAME_RESP_FILTER = " > Фильтр ответа";

    public ResponseFilterController(SysResponseService service, SystemsService systemsService, SysPermissionsService sysPermissionsService, DocumentTypeService documentTypeService) {
        this.service = service;
        this.systemsService = systemsService;
        this.sysPermissionsService = sysPermissionsService;
        this.documentTypeService = documentTypeService;
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId) {
        return new Content().setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId)
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter/add")
                                )
                ))
                .setTable(getTableAll(systemId, parameterValId));
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        return getContentView(systemId, parameterValId, id);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}/edit")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        SysResponse sysResponse = service.getById(id);
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId) + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id)
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id)
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Тип документа")
                                .setName("document_type_id")
                                .setValue(sysResponse.getDocumentType().getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Путь к полю в JSON или оригинальный JSON")
                                .setName("document_fact_key")
                                .setValue(sysResponse.getDocumentFactKey())
                )));
    }

    @PutMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdScopeDto")
    public Content save(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id,
                        @RequestBody ResponseFilerDto responseFilerDto) {
        service.edit(service.getById(id), documentTypeService.getById(responseFilerDto.getDocumentTypeId()), responseFilerDto.getDocumentFactKey());
        return getContentView(systemId, parameterValId, id).setNotifications(List.of(new Notification()
                .setType(NotificationType.INFO)
                .setMessage("Фильтр ответа успешно изменен")
        ));
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add(@PathVariable("system_id") long systemId,
                       @PathVariable("parameter_val_id") long parameterValId) {
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId) + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.SELECT)
                                .setLabel("Тип документа")
                                .setName("document_type_id")
                                .setValues(documentTypeService.getAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .toList()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Путь к полю в JSON или оригинальный JSON")
                                .setName("document_fact_key")
                                .setPlaceholder("OriginalJSON")
                )));
    }

    @PostMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackScopeDto")
    public Content create(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @RequestBody ResponseFilerDto responseFilerDto) {
        SysResponse sysResponse = service.add(
                sysPermissionsService.getById(parameterValId),
                documentTypeService.getById(responseFilerDto.getDocumentTypeId()),
                responseFilerDto.getDocumentFactKey()
        );
        return getContentView(systemId, parameterValId, sysResponse.getId())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage("Фильтр успешно добавлена")
                ));
    }

    @DeleteMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/response_filter/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Фильтр успешно удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления фильтра");
        }
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId)
                                ),
                        new Button().setTitle("Добавить запись")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter/add")
                                )
                ))
                .setTable(getTableAll(systemId, parameterValId))
                .setNotifications(List.of(notification));
    }

    private ValueItem toValueItem(DocumentType documentType) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(documentType.getId());
        valueItem.setValue(documentType.getMnemonic() + " (" + documentType.getName() + ")");
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

    private Content fallbackIdScopeDto(long id, ScopeDto scopeDto) {
        return fallback();
    }

    private Content fallbackScopeDto(ScopeDto scopeDto) {
        return fallback();
    }

    private Table getTableAll(long systemId, long parameterValId) {
        List<Row> rows = new ArrayList<>();
        for (SysResponse sysResponse : sysPermissionsService.getById(parameterValId).getSysResponseList()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/systems/" + systemId
                                    + "/parameter_val/" + parameterValId
                                    + "/response_filter/" + sysResponse.getId())
                    )
                    .setColumns(List.of(
                            sysResponse.getDocumentType().getMnemonic(),
                            sysResponse.getDocumentFactKey() == null ? "" : sysResponse.getDocumentFactKey()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Тип документа", "Путь к полю в JSON или оригинальный JSON"))
                .setRows(rows);
    }

    private Content getContentView(long systemId, long parameterValId, long id) {
        SysResponse sysResponse = service.getById(id);
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id + "/edit")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id)
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SELECT)
                                .setLabel("Тип документа")
                                .setName("document_type_id")
                                .setSelectedId(sysResponse.getDocumentType().getId())
                                .setValues(documentTypeService.getAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .toList()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Путь к полю в JSON или оригинальный JSON")
                                .setName("document_fact_key")
                                .setValue(sysResponse.getDocumentFactKey())
                ));
    }

    private String getBasePageName(long systemId, long parameterValId) {
        return PAGE_NAME + systemsService.getById(systemId).getName() + PAGE_NAME_PAR_VAL + sysPermissionsService.getById(parameterValId).getComparing() + PAGE_NAME_RESP_FILTER;
    }
}