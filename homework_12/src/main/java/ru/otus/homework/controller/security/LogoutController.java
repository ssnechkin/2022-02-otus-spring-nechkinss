package ru.otus.homework.controller.security;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.enums.FieldType;

import java.util.List;

@RestController
public class LogoutController {

    @GetMapping("/logout")
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
        rememberMe.setLabel("Запомнить меня.");

        form.setDataType(FormDataType.FORM_DATA);
        form.setFields(List.of(username, password, rememberMe));
        content.setForm(form);
        return content;
    }
}
