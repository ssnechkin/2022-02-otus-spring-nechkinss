package ru.dpec.service.ui;

import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.entity.Menu;
import ru.dpec.domain.entity.security.UserDetail;

import java.util.List;

public interface MenuUiService {

    List<Menu> getAllMenu();

    Content getMenu(UserDetail userDetail);
}
