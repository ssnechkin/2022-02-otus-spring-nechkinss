package ru.rncb.dpec.service.ui.systems.parameterval.sub;

import ru.rncb.dpec.domain.dto.in.dp.systems.parameterval.sub.RequestedDocumentsDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface RequestedDocumentsUiService {
    Content list(long systemId, long parameterValId);

    Content view(long systemId, long parameterValId, long id);

    Content add(long systemId, long parameterValId);

    Content create(long systemId, long parameterValId, RequestedDocumentsDto requestedDocumentsDto);

    Content delete(long systemId, long parameterValId, long id);

    Content getContentView(long systemId, long parameterValId, long id);
}
