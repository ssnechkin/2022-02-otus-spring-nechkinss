package ru.rncb.dpec.controller.dp.handbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Button;
import ru.rncb.dpec.domain.dto.out.content.Form;
import ru.rncb.dpec.domain.dto.out.content.Link;
import ru.rncb.dpec.domain.dto.out.content.Notification;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HandbookController {

    private final static String PAGE_NAME = "Справочники";

    public HandbookController(MenuRepository menuRepository) {
        addMenu(menuRepository);
    }

    @GetMapping("/handbook")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content list() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button().setTitle("Типы документов")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/document_type")
                ));
        buttons.add(new Button().setTitle("Области доступа")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/scope")
                ));
        buttons.add(new Button().setTitle("Цели")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/purposes")
                ));
        buttons.add(new Button().setTitle("Действия")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/actions")
                ));
        return new Content().setPageName(PAGE_NAME)
                .setManagement(buttons)
                .setTable(new Table())
                .setForm(new Form());
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private void addMenu(MenuRepository menuRepository) {
        if (menuRepository.findByLink("/handbook").size() == 0) {
            Menu menu = new Menu().setTitle(PAGE_NAME)
                    .setPosition(3)
                    .setMethod(HttpMethod.GET.name())
                    .setLink("/handbook")
                    .setAlt(true);
            menuRepository.save(menu);
        }
    }
}