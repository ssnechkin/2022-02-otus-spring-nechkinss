package ru.dpec.service.ui.administration;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.in.UserDto;

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
