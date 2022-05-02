package ru.otus.homework.domain.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldEntity {
    private String type, label, name;
    private Object value;
}
