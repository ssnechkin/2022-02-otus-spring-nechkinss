package ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.sub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseFilerDto {

    @JsonProperty("document_type_id")
    private long documentTypeId;

    @JsonProperty("document_fact_key")
    private String documentFactKey="OriginalJSON";
}
