package ru.dpec.service.ui.permissions;

import ru.dpec.domain.dto.in.dp.permissions.PermissionsActionsDto;
import ru.dpec.domain.dto.out.Content;

public interface PermissionsActionsUiService {
    Content list(long permissionsId);

    Content view(long permissionsId, long id);

    Content add(long permissionsId);

    Content create(long permissionsId, PermissionsActionsDto permissionsActionsDto);

    Content delete(long permissionsId, long id);

    Content getContentView(long permissionsId, long id);
}
