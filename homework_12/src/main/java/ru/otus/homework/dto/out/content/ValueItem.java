package ru.otus.homework.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueItem {
    private long id;
    private String value;
}
