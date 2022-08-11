package ru.rncb.dpec.service.ui.handbook;

import ru.rncb.dpec.domain.dto.in.dp.handbook.ScopeDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface ScopeUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, ScopeDto scopeDto);

    Content add();

    Content create(ScopeDto scopeDto);

    Content delete(long id);

    Content getContentView(long id);
}