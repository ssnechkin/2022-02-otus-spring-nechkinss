package ru.rncb.dpec.service.ui.handbook;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.Color;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.handbook.ScopeService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeUiServiceImpl implements DocumentTypeUiService {

    private final DocumentTypeService service;
    private final ScopeService scopeService;
    private final static String PAGE_NAME = "Справочники > Типы документов";

    public DocumentTypeUiServiceImpl(DocumentTypeService service, ScopeService scopeService) {
        this.service = service;
        this.scopeService = scopeService;
    }

    @Override
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @Override
    public Content view(long id) {
        return getContentView(id);
    }

    @Override
    public Content edit(long id) {
        DocumentType documentType = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/document_type/" + documentType.getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type/" + documentType.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(documentType.getMnemonic()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(documentType.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Источник данных (ведомство)")
                                .setName("source")
                                .setValue(documentType.getSource()),
                        new Field().setType(FieldType.SELECT)
                                .setLabel("Область доступа")
                                .setName("scope")
                                .setSelectedId(documentType.getScope().getId())
                                .setValues(scopeService.getAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .collect(Collectors.toList()))
                )));
    }

    @Override
    public Content save(long id, DocumentTypeDto documentTypeDto) {
        if (documentTypeDto.getMnemonic() == null || documentTypeDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), documentTypeDto.getMnemonic(), documentTypeDto.getName(),
                    scopeService.getById(documentTypeDto.getScope()), documentTypeDto.getSource());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Тип документа успешно сохранен")
            ));
        }
    }

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setColor(Color.green)
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/document_type")
                                ),
                        new Button().setTitle("Отмена")
                                .setColor(Color.cyan)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type")
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
                                        .setLabel("Источник документа (ведомство)")
                                        .setName("source"),
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Область действия (scope)")
                                        .setName("scope")
                                        .setValues(scopeService.getAll()
                                                .stream()
                                                .map(this::toValueItem)
                                                .collect(Collectors.toList()))
                        ))
                );
    }

    @Override
    public Content create(DocumentTypeDto documentTypeDto) {
        if (documentTypeDto.getMnemonic() == null || documentTypeDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            DocumentType documentType = service.add(documentTypeDto.getMnemonic(), documentTypeDto.getName(),
                    scopeService.getById(documentTypeDto.getScope()), documentTypeDto.getSource());
            return getContentView(documentType.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Тип документа успешно добавлен")
                    ));
        }
    }

    @Override
    public Content delete(long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Тип документа успешно удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления типа документа");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long id) {
        DocumentType documentType = service.getById(id);
        if (documentType == null) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Тип документа отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setColor(Color.cyan)
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type")
                                ),
                        new Button().setTitle("Редактировать")
                                .setColor(Color.cyan)
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type/edit/" + documentType.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setColor(Color.red)
                                .setPosition(3)
                                .setConfirm("Подтверждаете удаление?")
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/document_type/" + documentType.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Мнемоника")
                                .setName("mnemonic")
                                .setValue(documentType.getMnemonic()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(documentType.getName()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Источник данных (ведомство)")
                                .setName("source")
                                .setValue(documentType.getSource()),
                        new Field().setType(FieldType.SPAN)
                                .setLabel("Область доступа")
                                .setName("scope")
                                .setValue(documentType.getScope().getName())
                ));
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Назад")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/")
                        ),
                new Button().setTitle("Добавить запись")
                        .setColor(Color.cyan)
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/document_type/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (DocumentType documentType : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/handbook/document_type/" + documentType.getId())
                    )
                    .setColumns(List.of(
                            documentType.getMnemonic(),
                            documentType.getName() == null ? "" : documentType.getName(),
                            documentType.getScope() == null ? "" : documentType.getScope().getName()
                                    + " (" + documentType.getScope().getDescription() + ")",
                            documentType.getSource() == null ? "" : documentType.getSource()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Наименование", "Область доступа", "Источник данных"))
                .setRows(rows);
    }

    private ValueItem toValueItem(Scope scope) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(scope.getId());
        valueItem.setValue(scope.getName() + " (" + scope.getDescription() + ")");
        return valueItem;
    }
}