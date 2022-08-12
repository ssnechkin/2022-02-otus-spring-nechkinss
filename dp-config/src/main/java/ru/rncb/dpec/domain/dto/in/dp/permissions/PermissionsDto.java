package ru.rncb.dpec.domain.dto.in.dp.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PermissionsDto {
    private String mnemonic, name, responsibleobject, description;
    @JsonProperty("permission_expire")
    private long permissionExpire;
}
