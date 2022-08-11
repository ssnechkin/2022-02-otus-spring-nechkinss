package ru.rncb.dpec.service.ui;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.Button;
import ru.rncb.dpec.domain.dto.out.content.Link;
import ru.rncb.dpec.domain.dto.out.content.TopRight;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.security.UserDetail;
import ru.rncb.dpec.repository.MenuRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
public class MenuUiServiceImpl implements MenuUiService{
    private final MenuRepository menuRepository;

    public MenuUiServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }

    @Override
    public Content getMenu(UserDetail userDetail) {
        Content content = new Content();
        TopRight topRight = new TopRight();
        Button logout = new Button();
        logout.setTitle("Выйти");
        logout.setLink(new Link(HttpMethod.POST, "/logout"));
        topRight.setButtons(List.of(logout));
        topRight.setText(userDetail != null ? userDetail.getPublicName() : "");
        content.setTopRight(topRight);
        content.setButtons(getAssembledMenu());
        content.setManagement(List.of());
        content.setPageName("Конфигуратор интеграционных сервисов цифрового профиля ЕСИА");
        content.setFields(List.of());
        return content;
    }

    private List<Button> getAssembledMenu() {
        List<Button> buttons = new LinkedList<>();
        System.out.println(getAllMenu().size());
        for (Menu menu : getAllMenu()) {
            buttons.add(new Button()
                    .setPosition(menu.getPosition())
                    .setTitle(menu.getTitle())
                    .setAlt(true)
                    .setLink(new Link()
                            .setValue(menu.getLink())
                            .setMethod(HttpMethod.valueOf(menu.getMethod()))
                    )
            );
        }
        Comparator<Button> comparator = (o1, o2) -> o1.getPosition() < o2.getPosition() ? -1 : 0;
        buttons.sort(comparator);
        return buttons;
    }
}
