package ru.dpec.service.ui.handbook;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.in.dp.handbook.ActionsDto;

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
