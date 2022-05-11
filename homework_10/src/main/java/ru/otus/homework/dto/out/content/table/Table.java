package ru.otus.homework.dto.out.content.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private List<String> labels;
    private List<Row> rows;
}
