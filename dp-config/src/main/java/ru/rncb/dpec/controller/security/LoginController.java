package ru.rncb.dpec.controller.security;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.enums.FieldType;
import ru.rncb.dpec.dto.out.enums.FormDataType;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.dto.out.content.*;

import java.util.List;

@RestController
public class LoginController {

    @GetMapping("/page/login")
    public Content pageLogin() {
        Content content = new Content();
        content.setTopRight(new TopRight());
        content.setButtons(List.of());
        Form form = new Form();
        Button save = new Button();
        Field username = new Field(), password = new Field(), rememberMe = new Field();
        content.setPageName("Вход");
        save.setTitle("Войти");
        save.setLink(new Link(HttpMethod.POST, "/page/login"));
        content.setManagement(List.of(save));

        username.setType(FieldType.INPUT);
        username.setLabel("Логин");
        username.setPlaceholder("Логин");
        username.setName("username");

        password.setType(FieldType.PASSWORD);
        password.setLabel("Пароль");
        password.setPlaceholder("Пароль");
        password.setName("password");

        rememberMe.setType(FieldType.CHECKBOX);
        rememberMe.setName("remember-me");
        rememberMe.setLabel("Запомнить меня на этом компьютере.");

        form.setDataType(FormDataType.FORM_DATA);
        form.setFields(List.of(username, password, rememberMe));
        content.setForm(form);
        return content;
    }

    @GetMapping("/error/login")
    public Content pageErrorLogin() {
        Content content = new Content();
        Notification notification;
        notification = new Notification(NotificationType.ERROR, "Введен не верный логин или пароль");
        content.setNotifications(List.of(notification));
        return content;
    }
}
