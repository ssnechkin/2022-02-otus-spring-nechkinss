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
}
