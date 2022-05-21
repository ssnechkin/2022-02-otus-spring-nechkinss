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
    private String label, name, value;
    private List<ValueItem> values;
}
