package ru.rncb.dpec.service.securaty;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.repository.MenuRepository;

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
