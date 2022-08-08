package ru.rncb.dpec.dto.out.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rncb.dpec.dto.out.enums.FormDataType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Form {

    @JsonProperty("fields")
    private List<Field> fields;

    private FormDataType dataType = FormDataType.JSON;

    public Form setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Form setDataType(FormDataType dataType) {
        this.dataType = dataType;
        return this;
    }
}
