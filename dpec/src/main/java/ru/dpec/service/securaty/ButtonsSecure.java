package ru.dpec.service.securaty;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.dpec.domain.entity.Menu;
import ru.dpec.repository.MenuRepository;

import java.util.List;

@Service
public class ButtonsSecure {
    private final MenuRepository menuRepository;

    public ButtonsSecure(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Menu> getMenu() {
        return menuRepository.findAll();
    }
}
