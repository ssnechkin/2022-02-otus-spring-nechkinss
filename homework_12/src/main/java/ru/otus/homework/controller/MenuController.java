package ru.otus.homework.controller;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.domain.entity.security.UserDetail;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;
import ru.otus.homework.dto.out.content.TopRight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class MenuController {

    private final List<MenuItems> menuItems;

    public MenuController(List<MenuItems> menuItems) {
        this.menuItems = menuItems;
    }

    @GetMapping("/menu")
    public Content getMenu(@AuthenticationPrincipal UserDetail userDetail) {
        Content content = new Content();
        TopRight topRight = new TopRight();
        Button logout = new Button();
        logout.setTitle("Выйти");
        logout.setLink(new Link(HttpMethod.POST, "/logout"));
        topRight.setButtons(List.of(logout));
        topRight.setText(userDetail != null ? userDetail.getPublicName() : "");
        content.setTopRight(topRight);
        content.setButtons(addMenuInModel(menuItems));
        content.setManagement(List.of());
        content.setPageName("Добро пожаловать в библиотеку книг");
        content.setFields(List.of());
        return content;
    }

    private List<Button> addMenuInModel(List<MenuItems> menuItems) {
        List<Button> buttons = new ArrayList<>();
        for (MenuItems items : menuItems) {
            buttons.addAll(items.getMenu());
        }
        Comparator<Button> comparator = (o1, o2) -> o1.getPosition() >= o2.getPosition() ? -1 : 0;
        buttons.sort(comparator);
        return buttons;
    }
}
