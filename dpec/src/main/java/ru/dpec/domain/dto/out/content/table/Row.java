package ru.dpec.domain.dto.out.content.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dpec.domain.dto.out.content.Link;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row {
    private Link link;
    private List<String> columns;

    public Row setLink(Link link) {
        this.link = link;
        return this;
    }

    public Row setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }
}
