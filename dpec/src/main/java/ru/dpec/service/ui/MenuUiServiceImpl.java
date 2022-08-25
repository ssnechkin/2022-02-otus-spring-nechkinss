package ru.dpec.service.ui;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Field;
import ru.dpec.domain.dto.out.enums.FieldType;
import ru.dpec.domain.entity.Menu;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.domain.dto.out.content.Link;
import ru.dpec.domain.dto.out.content.TopRight;
import ru.dpec.domain.entity.security.UserDetail;
import ru.dpec.service.securaty.ButtonsSecure;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
public class MenuUiServiceImpl implements MenuUiService {
    private final ButtonsSecure buttonsSecure;

    public MenuUiServiceImpl(ButtonsSecure buttonsSecure) {
        this.buttonsSecure = buttonsSecure;
    }

    @Override
    public List<Menu> getAllMenu() {
        return buttonsSecure.getMenu();
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
        content.setFields(List.of(
                new Field()
                        .setType(FieldType.SPAN)
                        .setLabel("")
                        .setValue("Приложение предназначенно для настраивания правил формирования json-запросов " +
                                "интеграционными сервисами esia_digital_profile, а также настраивания JSON " +
                                "в ответных сообщениях сервисов.")
        ));
        return content;
    }

    private List<Button> getAssembledMenu() {
        List<Button> buttons = new LinkedList<>();
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