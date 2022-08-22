package ru.dpec.service.ui.handbook;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.in.dp.handbook.ScopeDto;

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
