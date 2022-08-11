package ru.rncb.dpec.service.ui.systems;

import ru.rncb.dpec.domain.dto.in.dp.systems.SystemsDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface SystemsUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, SystemsDto systemsDto);

    Content add();

    Content create(SystemsDto systemsDto);

    Content delete(long id);

    Content delete(long systemsId, long id);

    Content getContentView(long systemId);
}
