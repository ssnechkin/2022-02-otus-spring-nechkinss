package ru.otus.homework.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.homework.dto.out.enums.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private FieldType type;
    private String label, name, value, placeholder;
    private List<ValueItem> values;

    public Field setType(FieldType type) {
        this.type = type;
        return this;
    }

    public Field setLabel(String label) {
        this.label = label;
        return this;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public Field setValue(String value) {
        this.value = value;
        return this;
    }

    public Field setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public Field setValues(List<ValueItem> values) {
        this.values = values;
        return this;
    }
}
