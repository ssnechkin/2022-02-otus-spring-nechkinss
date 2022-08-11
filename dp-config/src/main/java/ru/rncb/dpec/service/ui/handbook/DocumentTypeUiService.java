package ru.rncb.dpec.service.ui.handbook;

import ru.rncb.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface DocumentTypeUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, DocumentTypeDto documentTypeDto);

    Content add();

    Content create(DocumentTypeDto documentTypeDto);

    Content delete(long id);

    Content getContentView(long id);
}
