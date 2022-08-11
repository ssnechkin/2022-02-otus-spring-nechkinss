package ru.rncb.dpec.domain.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    private String username, password;

    @JsonProperty("public_name")
    private String publicName;

    @JsonProperty("role_id")
    private long roleId;
}
