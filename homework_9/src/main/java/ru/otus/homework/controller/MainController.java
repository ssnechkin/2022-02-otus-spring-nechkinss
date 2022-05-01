package ru.otus.homework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.homework.domain.ui.MenuItem;

import java.util.List;
import java.util.TreeMap;

@Controller
public class MainController {

    private final List<MenuEntity> menuEntities;

    public MainController(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    @GetMapping("/")
    public String main(Model model) {
        addMenuInModel(model);
        return "index";
    }

    private void addMenuInModel(Model model) {
        TreeMap<Integer, MenuItem> menus = new TreeMap<>();
        for (MenuEntity menuEntity : menuEntities) {
            for (MenuItem menu : menuEntity.getMenuList()) {
                menus.put(menu.getPosition(), menu);
            }
        }
        model.addAttribute("menu_list", menus);
    }
}
