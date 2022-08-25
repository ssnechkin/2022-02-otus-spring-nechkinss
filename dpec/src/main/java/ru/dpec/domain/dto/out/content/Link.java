package ru.dpec.domain.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private HttpMethod method;
    private String value;

    public Link setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public Link setValue(String value) {
        this.value = value;
        return this;
    }
}
