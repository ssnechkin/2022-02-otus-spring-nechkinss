package ru.dpec.service.ui.handbook;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;

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
