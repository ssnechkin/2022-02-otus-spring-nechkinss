package ru.dpec.service.ui.permissions;

import ru.dpec.domain.dto.in.dp.permissions.PermissionsScopeDto;
import ru.dpec.domain.dto.out.Content;

public interface PermissionsScopeUiService {
    Content list(long permissionsId);

    Content view(long permissionsId, long id);

    Content add(long permissionsId);

    Content create(long permissionsId, PermissionsScopeDto permissionsScopeDto);

    Content delete(long permissionsId, long id);

    Content getContentView(long permissionsId, long id);
}
