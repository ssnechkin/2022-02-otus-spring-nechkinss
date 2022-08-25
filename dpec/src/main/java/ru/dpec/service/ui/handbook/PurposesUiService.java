package ru.dpec.service.ui.handbook;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.in.dp.handbook.PurposesDto;

public interface PurposesUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, PurposesDto purposesDto);

    Content add();

    Content create(PurposesDto purposesDto);

    Content delete(long id);

    Content getContentView(long id);
}
