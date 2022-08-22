package ru.dpec.domain.dto.out.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dpec.domain.dto.out.enums.Color;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button {
    private int position;
    private String title;
    private String confirm;
    private Link link;
    private Boolean alt;
    private Color color;

    public Button setColor(Color color) {
        this.color = color;
        return this;
    }

    public Button setPosition(int position) {
        this.position = position;
        return this;
    }

    public Button setConfirm(String confirm) {
        this.confirm = confirm;
        return this;
    }

    public Button setTitle(String title) {
        this.title = title;
        return this;
    }

    public Button setLink(Link link) {
        this.link = link;
        return this;
    }

    public Button setAlt(Boolean alt) {
        this.alt = alt;
        return this;
    }
}
