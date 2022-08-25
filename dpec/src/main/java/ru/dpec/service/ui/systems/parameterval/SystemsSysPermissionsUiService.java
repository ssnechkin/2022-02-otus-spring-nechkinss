package ru.dpec.service.ui.systems.parameterval;

import ru.dpec.domain.dto.in.dp.systems.parameterval.SystemsUrlParameterValDto;
import ru.dpec.domain.dto.out.Content;

public interface SystemsSysPermissionsUiService {
    Content addUrlParameterVal(long systemsId);

    Content view(long systemsId, long id);

    Content createParamVal(long systemsId, SystemsUrlParameterValDto urlParameterValDto);

    Content edit(long systemsId, long id);

    Content save(long systemsId, long id, SystemsUrlParameterValDto urlParameterValDto);

    Content getContentView(long systemsId, long sysPermissionsId);
}
