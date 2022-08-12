package ru.rncb.dpec.service.ui.systems.parameterval.sub;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.sub.ResponseFilerDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.Color;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.SysResponse;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.systems.SysPermissionsService;
import ru.rncb.dpec.service.dp.systems.SysResponseService;
import ru.rncb.dpec.service.dp.systems.SystemsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseFilterUiServiceImpl implements ResponseFilterUiService{

    private final SysResponseService service;
    private final SystemsService systemsService;
    private final SysPermissionsService sysPermissionsService;
    private final DocumentTypeService documentTypeService;
    private final static String PAGE_NAME = "Системы > ";
    private final static String PAGE_NAME_PAR_VAL = " > Значение параметра: ";
    private final static String PAGE_NAME_RESP_FILTER = " > Фильтр ответа";

    public ResponseFilterUiServiceImpl(SysResponseService service, SystemsService systemsService, SysPermissionsService sysPermissionsService, DocumentTypeService documentTypeService) {
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
    public Content edit(long systemId, long parameterValId, long id) {
        SysResponse sysResponse = service.getById(id);
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId) + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id)
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content save(long systemId, long parameterValId, long id, ResponseFilerDto responseFilerDto) {
        service.edit(service.getById(id), documentTypeService.getById(responseFilerDto.getDocumentTypeId()), responseFilerDto.getDocumentFactKey());
        return getContentView(systemId, parameterValId, id).setNotifications(List.of(new Notification()
                .setType(NotificationType.INFO)
                .setMessage("Фильтр ответа успешно изменен")
        ));
    }

    @Override
    public Content add(long systemId, long parameterValId) {
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId) + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
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

    @Override
    public Content create(long systemId, long parameterValId, ResponseFilerDto responseFilerDto) {
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

    @Override
    public Content delete(long systemId, long parameterValId, long id) {
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
                .setManagement(getBaseManagement(systemId, parameterValId))
                .setTable(getTableAll(systemId, parameterValId))
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long systemId, long parameterValId, long id) {
        SysResponse sysResponse = service.getById(id);
        if(sysResponse == null){
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Фильтр отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(getBasePageName(systemId, parameterValId))
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId + "/response_filter")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/systems/" + systemId
                                                + "/parameter_val/" + parameterValId
                                                + "/response_filter/" + id + "/edit")
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
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

    private ValueItem toValueItem(DocumentType documentType) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(documentType.getId());
        valueItem.setValue(documentType.getMnemonic() + " (" + documentType.getName() + ")");
        return valueItem;
    }

    private List<Button> getBaseManagement(long systemId, long parameterValId) {
        return List.of(
                new Button().setTitle("Назад")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/systems/" + systemId
                                        + "/parameter_val/" + parameterValId)
                        ),
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/systems/" + systemId
                                        + "/parameter_val/" + parameterValId + "/response_filter/add")
                        )
        );
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

    private String getBasePageName(long systemId, long parameterValId) {
        return PAGE_NAME + systemsService.getById(systemId).getName() + PAGE_NAME_PAR_VAL + sysPermissionsService.getById(parameterValId).getComparing() + PAGE_NAME_RESP_FILTER;
    }
}