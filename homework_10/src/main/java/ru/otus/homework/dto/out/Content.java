package ru.otus.homework.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.Form;
import ru.otus.homework.dto.out.content.Menu;
import ru.otus.homework.dto.out.content.Notification;
import ru.otus.homework.dto.out.content.table.Table;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {

    @JsonProperty("menu")
    private List<Menu> menu;

    @JsonProperty("page_name")
    private String pageName;

    @JsonProperty("management")
    private List<Menu> management;

    @JsonProperty("table")
    private Table table;

    @JsonProperty("fields")
    private List<Field> fields;

    @JsonProperty("form")
    private Form form;

    @JsonProperty("notifications")
    private List<Notification> notifications;
}
