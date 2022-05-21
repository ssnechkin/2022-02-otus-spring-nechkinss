package ru.otus.homework.dto.out.content.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.homework.dto.out.content.Link;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row {
    private Link link;
    private List<String> columns;
}
