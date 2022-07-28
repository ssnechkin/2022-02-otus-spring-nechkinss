package ru.otus.homework.dto.out.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopRight {
    @JsonProperty("buttons")
    private List<Button> buttons;
    private String text;

    public TopRight setButtons(List<Button> buttons) {
        this.buttons = buttons;
        return this;
    }

    public TopRight setText(String text) {
        this.text = text;
        return this;
    }
}
