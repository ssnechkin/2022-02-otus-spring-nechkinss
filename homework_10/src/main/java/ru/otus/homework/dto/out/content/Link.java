package ru.otus.homework.dto.out.content;

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
}
