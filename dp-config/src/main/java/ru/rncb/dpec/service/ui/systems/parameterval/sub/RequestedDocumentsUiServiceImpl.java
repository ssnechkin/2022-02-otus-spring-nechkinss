package ru.rncb.dpec.service.ui.systems.parameterval.sub;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.sub.RequestedDocumentsDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.service.dp.documents.RequestedDocumentsService;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.systems.SysPermissionsService;
import ru.rncb.dpec.service.dp.systems.SystemsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestedDocumentsUiServiceImpl implements RequestedDocumentsUiService {

    private final RequestedDocumentsService service;
    private final SystemsService systemsService;
    private final SysPermissionsService sysPermissionsService;
    private final DocumentTypeService documentTypeService;
    private final static String PAGE_NAME = "Системы > ";
    private final static String PAGE_NAME_PAR_VAL = " > Значение параметра: ";
    private final static String PAGE_NAME_REQ_DOC = " > Список запрашиваемых документов";

    public RequestedDocumentsUiServiceImpl(RequestedDocumentsService service, SystemsService systemsService,
                                           SysPermissionsService sysPermissionsService, DocumentTypeService documentTypeService) {
        this.service = service;
        this.systemsService = systemsService;
        this.sysPermissionsService = sysPermissionsService;
        this.documentTypeService = documentTypeService;
    }

    @Override
    public Content list(long systemId, long parameterValId) {
        return new Content().setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(getBaseManagement(systemId, parameterValId))
                .setTable(getTableAll(systemId, parameterValId));
    }

    @Override
    public Content view(long systemId, long parameterValId, long id) {
        return getContentView(systemId, parameterValId, id);
    }

    @Override
    public Content add(long systemId, long parameterValId) {
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
                                        .setName("api_version")
                                        .setPlaceholder("v1/v2"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Тип запрашиваемого файла XML/PDF")
                                        .setName("file_type")
                                        .setPlaceholder("XML/PDF/Пусто-JSON"),
                                new Field().setType(FieldType.CHECKBOX)
                                        .setLabel("Флаг расширенного запроса")
                                        .setName("is_extended")
                        ))
                );
    }

    @Override
    public Content create(long systemId, long parameterValId, RequestedDocumentsDto requestedDocumentsDto) {
        RequestedDocuments requestedDocuments = service.add(
                documentTypeService.getById(requestedDocumentsDto.getDocumentTypeId()),
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

    @Override
    public Content delete(long systemId, long parameterValId, long id) {
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
                .setManagement(getBaseManagement(systemId, parameterValId))
                .setTable(getTableAll(systemId, parameterValId))
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long systemId, long parameterValId, long id) {
        RequestedDocuments requestedDocuments = service.getById(id);
        if(requestedDocuments == null){
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Запрашиваемый документ отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
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
                                .setValue(requestedDocuments.getDocumentType().getMnemonic() + " (" + requestedDocuments.getDocumentType().getName() + ")"),
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

    private ValueItem toValueItem(DocumentType documentType) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(documentType.getId());
        valueItem.setValue(documentType.getMnemonic() + " (" + documentType.getName() + ")");
        return valueItem;
    }

    private List<Button> getBaseManagement(long systemId, long parameterValId) {
        return List.of(
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
        );
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
}