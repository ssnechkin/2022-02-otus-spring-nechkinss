package ru.rncb.dpec.service.ui.handbook;

import ru.rncb.dpec.domain.dto.in.dp.handbook.ActionsDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface ActionsUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, ActionsDto actionsDto);

    Content add();

    Content create(ActionsDto actionsDto);

    Content delete(long id);

    Content getContentView(long id);
}
