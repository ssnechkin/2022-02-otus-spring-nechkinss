package ru.rncb.dpec.controller.dp.systems.parameterval.sub;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.dto.in.dp.handbook.ScopeDto;
import ru.rncb.dpec.dto.in.dp.systems.parameterval.sub.RequestedDocumentsDto;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.*;
import ru.rncb.dpec.dto.out.content.table.Row;
import ru.rncb.dpec.dto.out.content.table.Table;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.service.dp.documents.RequestedDocumentsService;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.systems.SysPermissionsService;
import ru.rncb.dpec.service.dp.systems.SystemsService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RequestedDocumentsController {

    private final RequestedDocumentsService service;
    private final SystemsService systemsService;
    private final SysPermissionsService sysPermissionsService;
    private final DocumentTypeService documentTypeService;
    private final static String PAGE_NAME = "Системы -> ";
    private final static String PAGE_NAME_PAR_VAL = " -> Значение параметра: ";
    private final static String PAGE_NAME_REQ_DOC = " -> Список запрашиваемых документов";

    public RequestedDocumentsController(RequestedDocumentsService service, SystemsService systemsService,
                                        SysPermissionsService sysPermissionsService, DocumentTypeService documentTypeService) {
        this.service = service;
        this.systemsService = systemsService;
        this.sysPermissionsService = sysPermissionsService;
        this.documentTypeService = documentTypeService;
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents")
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
                                                + "/parameter_val/" + parameterValId
                                                + "/requested_documents/add")
                                )
                ))
                .setTable(getTableAll(systemId, parameterValId));
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("system_id") long systemId,
                        @PathVariable("parameter_val_id") long parameterValId,
                        @PathVariable("id") long id) {
        return getContentView(systemId, parameterValId, id);
    }

    @GetMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add(@PathVariable("system_id") long systemId,
                       @PathVariable("parameter_val_id") long parameterValId) {
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId) + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/requested_documents")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/requested_documents")
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
                                        .setLabel("Версия API (в ЕСИА)")
                                        .setName("api_version"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Тип запрашиваемого файла XML/PDF")
                                        .setName("file_type"),
                                new Field().setType(FieldType.CHECKBOX)
                                        .setLabel("Флаг расширенного запроса")
                                        .setName("is_extended")
                        ))
                );
    }

    @PostMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackScopeDto")
    public Content create(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @RequestBody RequestedDocumentsDto requestedDocumentsDto) {
        RequestedDocuments requestedDocuments = service.add(documentTypeService.getById(requestedDocumentsDto.getDocumentTypeId()),
                requestedDocumentsDto.getApiVersion(),
                requestedDocumentsDto.getFileType(),
                sysPermissionsService.getById(parameterValId).getRequestedDocumentsListName(),
                requestedDocumentsDto.isExtended()
        );
        return getContentView(systemId, parameterValId, requestedDocuments.getId())
                .setNotifications(List.of(
                        new Notification().setType(NotificationType.INFO)
                                .setMessage("Документ успешно добавлен в список для значения параметра "
                                        + sysPermissionsService.getById(parameterValId).getComparing())
                ));
    }

    @DeleteMapping("/systems/{system_id}/parameter_val/{parameter_val_id}/requested_documents/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("system_id") long systemId,
                          @PathVariable("parameter_val_id") long parameterValId,
                          @PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Документ успешно удален из списка");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления документа из списка");
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
                                                + "/parameter_val/" + parameterValId
                                                + "/requested_documents/add")
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
        String listName = sysPermissionsService.getById(parameterValId).getRequestedDocumentsListName();
        for (RequestedDocuments requestedDocuments : service.getAllByListName(listName)) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/systems/" + systemId + "/parameter_val/" + parameterValId
                                    + "/requested_documents/" + requestedDocuments.getId())
                    )
                    .setColumns(List.of(
                            requestedDocuments.getDocumentType().getMnemonic(),
                            requestedDocuments.getApiVersion() == null ? "" : requestedDocuments.getApiVersion(),
                            requestedDocuments.getFileType() == null ? "" : requestedDocuments.getFileType(),
                            (requestedDocuments.isExtended() ? "&check;" : "")
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Тип документа", "Версия API", "Тип файла", "Флаг расширенного запроса"))
                .setRows(rows);
    }

    private String getBasePageName(long systemId, long parameterValId) {
        return PAGE_NAME + systemsService.getById(systemId).getName() + PAGE_NAME_PAR_VAL + sysPermissionsService.getById(parameterValId).getComparing() + PAGE_NAME_REQ_DOC;
    }

    private Content getContentView(long systemId, long parameterValId, long id) {
        RequestedDocuments requestedDocuments = service.getById(id);
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId + "/parameter_val/" + parameterValId + "/requested_documents")
                                ),
                        new Button().setTitle("Удалить из списка")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/systems/" + systemId + "/parameter_val/" + parameterValId + "/requested_documents/" + requestedDocuments.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Тип документа")
                                .setName("document_type_id")
                                .setValue(requestedDocuments.getDocumentType().getMnemonic() + requestedDocuments.getDocumentType().getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Версия API (в ЕСИА)")
                                .setName("api_version")
                                .setValue(requestedDocuments.getApiVersion()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Тип запрашиваемого файла XML/PDF")
                                .setName("file_type")
                                .setValue(requestedDocuments.getFileType()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Флаг расширенного запроса")
                                .setName("is_extended")
                                .setValue(requestedDocuments.isExtended() ? "&check;" : "")
                ));
    }
}