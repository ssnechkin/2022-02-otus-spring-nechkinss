package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.rncb.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;
import ru.rncb.dpec.service.dp.handbook.DocumentTypeService;
import ru.rncb.dpec.service.dp.handbook.ScopeService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DocumentTypeController {

    private final DocumentTypeService service;
    private final ScopeService scopeService;
    private final static String PAGE_NAME = "Справочники > Типы документов";

    public DocumentTypeController(DocumentTypeService service, ScopeService scopeService) {
        this.service = service;
        this.scopeService = scopeService;
    }

    @GetMapping("/handbook/document_type")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @GetMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/handbook/document_type/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        DocumentType documentType = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/handbook/document_type/" + documentType.getId())
                                ),
                        new Button().setTitle("Отмена")
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
                                .setValues(scopeService.getAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .toList())
                )));
    }

    @PutMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdDocumentTypeDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody DocumentTypeDto documentTypeDto) {
        if (documentTypeDto.getMnemonic() == null || documentTypeDto.getMnemonic().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Мнемоника должна быть заполнена")
            ));
        } else {
            service.edit(service.getById(id), documentTypeDto.getMnemonic(), documentTypeDto.getName(), scopeService.getById(documentTypeDto.getScope()), documentTypeDto.getSource());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Тип документа успешно сохранен")
            ));
        }
    }

    @GetMapping("/handbook/document_type/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/handbook/document_type")
                                ),
                        new Button().setTitle("Отмена")
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
                                                .toList())
                        ))
                );
    }

    @PostMapping("/handbook/document_type")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackDocumentTypeDto")
    public Content create(@RequestBody DocumentTypeDto documentTypeDto) {
        if (documentTypeDto.getMnemonic() == null || documentTypeDto.getMnemonic().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Мнемоника должна быть заполнена")
                    ));
        } else {
            DocumentType documentType = service.add(documentTypeDto.getMnemonic(), documentTypeDto.getName(), scopeService.getById(documentTypeDto.getScope()), documentTypeDto.getSource());
            return getContentView(documentType.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Тип документа успешно добавлен")
                    ));
        }
    }

    @DeleteMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
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

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackId(long id) {
        return fallback();
    }

    private Content fallbackIdDocumentTypeDto(long id, DocumentTypeDto documentTypeDto) {
        return fallback();
    }

    private Content fallbackDocumentTypeDto(DocumentTypeDto documentTypeDto) {
        return fallback();
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Назад")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/handbook/")
                        ),
                new Button().setTitle("Добавить запись")
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
                            documentType.getScope() == null ? "" : documentType.getScope().getName() + " (" + documentType.getScope().getDescription() + ")",
                            documentType.getSource() == null ? "" : documentType.getSource()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Мнемоника", "Наименование", "Область доступа", "Источник данных"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
        DocumentType documentType = service.getById(id);
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/handbook/document_type/edit/" + documentType.getId())
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/handbook/document_type/" + documentType.getId())
                                )
                ))
                .setFields(List.of(
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
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Область доступа")
                                .setName("scope")
                                .setValue(documentType.getScope().getName())
                ));
    }

    private ValueItem toValueItem(Scope scope) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(scope.getId());
        valueItem.setValue(scope.getName() + " (" + scope.getDescription() + ")");
        return valueItem;
    }
}