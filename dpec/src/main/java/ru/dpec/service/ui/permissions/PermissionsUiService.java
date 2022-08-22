package ru.dpec.service.ui.permissions;

import ru.dpec.domain.dto.in.dp.permissions.PermissionsDto;
import ru.dpec.domain.dto.out.Content;

public interface PermissionsUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, PermissionsDto permissionsDto);

    Content add();

    Content create(PermissionsDto permissionsDto);

    Content delete(long id);

    Content getContentView(long id);
}
