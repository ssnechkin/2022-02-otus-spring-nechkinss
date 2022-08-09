package ru.rncb.dpec.dto.in.dp;

import lombok.Data;

@Data
public class PermissionsDto {

    private String mnemonic, name, description, responsibleobject;
    private long expire;
}
