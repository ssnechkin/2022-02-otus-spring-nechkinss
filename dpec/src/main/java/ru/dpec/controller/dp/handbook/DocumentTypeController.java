package ru.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;
import ru.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Notification;
import ru.dpec.domain.dto.out.enums.NotificationType;
import ru.dpec.service.ui.handbook.DocumentTypeUiService;

import java.util.List;

@RestController
public class DocumentTypeController {

    private final DocumentTypeUiService service;

    public DocumentTypeController(DocumentTypeUiService service) {
        this.service = service;
    }

    @GetMapping("/handbook/document_type")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        return service.list();
    }

    @GetMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content view(@PathVariable("id") long id) {
        return service.getContentView(id);
    }

    @GetMapping("/handbook/document_type/edit/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content edit(@PathVariable("id") long id) {
        return service.edit(id);
    }

    @PutMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackIdDocumentTypeDto")
    public Content save(@PathVariable("id") long id, @RequestBody DocumentTypeDto documentTypeDto) {
        return service.save(id, documentTypeDto);
    }

    @GetMapping("/handbook/document_type/add")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content add() {
        return service.add();
    }

    @PostMapping("/handbook/document_type")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackDocumentTypeDto")
    public Content create(@RequestBody DocumentTypeDto documentTypeDto) {
        return service.create(documentTypeDto);
    }

    @DeleteMapping("/handbook/document_type/{id}")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallbackId")
    public Content delete(@PathVariable("id") long id) {
        return service.delete(id);
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
}