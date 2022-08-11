package ru.rncb.dpec.service.ui.permissions;

import ru.rncb.dpec.domain.dto.in.dp.permissions.PermissionsActionsDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface PermissionsActionsUiService {
    Content list(long permissionsId);

    Content view(long permissionsId, long id);

    Content add(long permissionsId);

    Content create(long permissionsId, PermissionsActionsDto permissionsActionsDto);

    Content delete(long permissionsId, long id);

    Content getContentView(long permissionsId, long id);
}
