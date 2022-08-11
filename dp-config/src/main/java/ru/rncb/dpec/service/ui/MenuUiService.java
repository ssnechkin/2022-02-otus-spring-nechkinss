package ru.rncb.dpec.service.ui;

import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.security.UserDetail;

import java.util.List;

public interface MenuUiService {

    List<Menu> getAllMenu();

    Content getMenu(UserDetail userDetail);
}
