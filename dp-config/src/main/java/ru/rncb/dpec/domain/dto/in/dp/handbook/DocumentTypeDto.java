package ru.rncb.dpec.domain.dto.in.dp.handbook;

import lombok.Data;

@Data
public class DocumentTypeDto {
    private String mnemonic;
    private String name;
    private String source;
    private long scope;
}