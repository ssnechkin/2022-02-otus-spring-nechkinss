package ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.sub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestedDocumentsDto {

    @JsonProperty("document_type_id")
    private long documentTypeId;

    @JsonProperty("api_version")
    private String apiVersion="v1";

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("is_extended")
    private boolean isExtended;
}
