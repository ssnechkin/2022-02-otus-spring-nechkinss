package ru.rncb.dpec.service.ui.handbook;

import ru.rncb.dpec.domain.dto.in.dp.handbook.PurposesDto;
import ru.rncb.dpec.domain.dto.out.Content;

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
