package ru.dpec.domain.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.dpec.domain.dto.out.content.*;
import ru.dpec.domain.dto.out.content.table.Table;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {

    @JsonProperty("menu")
    private List<Button> buttons;

    @JsonProperty("top_right")
    private TopRight topRight;

    @JsonProperty("page_name")
    private String pageName;

    @JsonProperty("management")
    private List<Button> management;

    @JsonProperty("table")
    private Table table;

    @JsonProperty("fields")
    private List<Field> fields;

    @JsonProperty("form")
    private Form form;

    @JsonProperty("notifications")
    private List<Notification> notifications;

    public Content setButtons(List<Button> buttons) {
        this.buttons = buttons;
        return this;
    }

    public Content setTopRight(TopRight topRight) {
        this.topRight = topRight;
        return this;
    }

    public Content setPageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public Content setManagement(List<Button> management) {
        this.management = management;
        return this;
    }

    public Content setTable(Table table) {
        this.table = table;
        return this;
    }

    public Content setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Content setForm(Form form) {
        this.form = form;
        return this;
    }

    public Content setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }
}
