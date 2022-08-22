package ru.dpec.domain.dto.out.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dpec.domain.dto.out.enums.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private FieldType type;
    private String label, name, value, placeholder;
    private List<ValueItem> values;
    private boolean checked;

    @JsonProperty("selected_id")
    private Long selectedId;

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

    public Field setChecked(Boolean value) {
        this.checked = value;
        return this;
    }

    public Field setSelectedId(Long value) {
        this.selectedId = value;
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
