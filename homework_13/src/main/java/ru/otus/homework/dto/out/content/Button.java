package ru.otus.homework.dto.out.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button {
    private int position;
    private String title;
    private Link link;
    private Boolean alt;

    public Button setPosition(int position) {
        this.position = position;
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
