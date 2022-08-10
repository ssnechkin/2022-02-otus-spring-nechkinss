package ru.rncb.dpec.domain.dto.out.content.table;

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

    public Table setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public Table setRows(List<Row> rows) {
        this.rows = rows;
        return this;
    }
}
