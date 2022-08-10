package ru.rncb.dpec.dto.in.dp.systems;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SystemsUrlParameterValDto {

    @JsonProperty("url_parameter_value")
    private String urlParameterValue;

    private String responsibleobject;

    @JsonProperty("permissions_id")
    private long permissionId;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("permission_expire")
    private long permissionExpire;
}
