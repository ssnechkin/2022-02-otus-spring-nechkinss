package ru.rncb.dpec.dto.in.dp.handbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocumentTypeDto {

    private String mnemonic;
    private String name;
    @JsonProperty("file_type")
    private String fileType;
    private String source;
    private long scope;
}
