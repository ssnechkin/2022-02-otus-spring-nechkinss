package ru.rncb.dpec.service.ui.administration;

import ru.rncb.dpec.domain.dto.in.UserDto;
import ru.rncb.dpec.domain.dto.out.Content;

public interface UsersUiService {
    Content list();

    Content view(long id);

    Content edit(long id);

    Content save(long id, UserDto userDto);

    Content add();

    Content create(UserDto userDto);

    Content delete(long id);

    Content getContentView(long id);
}
