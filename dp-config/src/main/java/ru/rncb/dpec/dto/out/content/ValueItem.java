package ru.rncb.dpec.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueItem {
    private long id;
    private String value;

    public ValueItem setId(long id) {
        this.id = id;
        return this;
    }

    public ValueItem setValue(String value) {
        this.value = value;
        return this;
    }
}
