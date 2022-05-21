package ru.otus.homework.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.dto.out.content.Menu;
import ru.otus.homework.dto.out.Content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class MainController {

    private final List<MenuItems> menuItems;

    public MainController(List<MenuItems> menuItems) {
        this.menuItems = menuItems;
    }

    @PostMapping("/")
    public Content getContent() {
        Content content = new Content();
        content.setMenu(addMenuInModel(menuItems));
        return content;
    }

    private List<Menu> addMenuInModel(List<MenuItems> menuItems) {
        List<Menu> menus = new ArrayList<>();
        for (MenuItems items : menuItems) {
            menus.addAll(items.getMenu());
        }
        Comparator<Menu> comparator = new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getPosition() >= o2.getPosition() ? -1 : 0;
            }
        };
        menus.sort(comparator);
        return menus;
    }
}
