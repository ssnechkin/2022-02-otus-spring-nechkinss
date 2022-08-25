package ru.dpec.service.ui.systems.parameterval.sub;

import ru.dpec.domain.dto.in.dp.systems.parameterval.sub.ResponseFilerDto;
import ru.dpec.domain.dto.out.Content;

public interface ResponseFilterUiService {
    Content list(long systemId, long parameterValId);

    Content view(long systemId, long parameterValId, long id);

    Content edit(long systemId, long parameterValId, long id);

    Content save(long systemId, long parameterValId, long id, ResponseFilerDto responseFilerDto);

    Content add(long systemId, long parameterValId);

    Content create(long systemId, long parameterValId, ResponseFilerDto responseFilerDto);

    Content delete(long systemId, long parameterValId, long id);

    Content getContentView(long systemId, long parameterValId, long id);
}
