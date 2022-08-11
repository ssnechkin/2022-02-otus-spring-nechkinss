package ru.rncb.dpec.service.ui.administration;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.dto.in.UserDto;
import ru.rncb.dpec.domain.dto.out.Content;
import ru.rncb.dpec.domain.dto.out.content.*;
import ru.rncb.dpec.domain.dto.out.content.table.Row;
import ru.rncb.dpec.domain.dto.out.content.table.Table;
import ru.rncb.dpec.domain.dto.out.enums.FieldType;
import ru.rncb.dpec.domain.dto.out.enums.NotificationType;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;
import ru.rncb.dpec.domain.entity.security.UserDetail;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.repository.security.RoleRepository;
import ru.rncb.dpec.security.UserService;

import java.util.*;

@Service
public class UsersUiServiceImpl implements UsersUiService {

    private final UserService service;
    private final RoleRepository roleRepository;
    private final static String PAGE_NAME = "Пользователей";

    public UsersUiServiceImpl(UserService service, MenuRepository menuRepository, RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;
        addMenu(menuRepository);
    }

    @Override
    public Content list() {
        return new Content().setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll());
    }

    @Override
    public Content view(long id) {
        return getContentView(id);
    }

    @Override
    public Content edit(long id) {
        Optional<UserDetail> userDetail = service.getById(id);
        if (!userDetail.isPresent()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Пользователь не найден")
            ));
        }
        return new Content()
                .setPageName(PAGE_NAME + " - редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/users/" + userDetail.get().getId())
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/users/" + userDetail.get().getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя уч. записи (login)")
                                .setName("username")
                                .setValue(userDetail.get().getUsername()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя пользователя")
                                .setName("public_name")
                                .setValue(userDetail.get().getPublicName()),
                        new Field().setType(FieldType.PASSWORD)
                                .setLabel("Пароль")
                                .setName("password")
                                .setValue(userDetail.get().getPassword()),
                        new Field().setType(FieldType.SELECT)
                                .setLabel("Роли")
                                .setName("role_id")
                                .setSelectedId(userDetail.get().getRoles().stream().findFirst().get().getId())
                                .setValues(roleRepository.findAll()
                                        .stream()
                                        .map(this::toValueItem)
                                        .toList())
                )));
    }

    @Override
    public Content save(long id, UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Имя уч. записи должно быть заполнено")
            ));
        } else {
            UserDetail userDetail = service.getById(id).get();
            Set<RoleGrantedAuthority> rolesSet = new HashSet<>();
            rolesSet.add(roleRepository.findById(userDto.getRoleId()).get());
            service.edit(userDetail, userDto.getPublicName(), userDto.getUsername(), userDto.getPassword(), rolesSet);
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Данные пользователя сохранены")
            ));
        }
    }

    @Override
    public Content add() {
        return new Content()
                .setPageName(PAGE_NAME + " - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/users")
                                ),
                        new Button().setTitle("Отмена")
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/users")
                                )

                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Имя уч. записи (login)")
                                        .setName("username"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Имя пользователя")
                                        .setName("public_name"),
                                new Field().setType(FieldType.PASSWORD)
                                        .setLabel("Пароль")
                                        .setName("password"),
                                new Field().setType(FieldType.SELECT)
                                        .setLabel("Роли")
                                        .setName("role_id")
                                        .setValues(roleRepository.findAll()
                                                .stream()
                                                .map(this::toValueItem)
                                                .toList())
                        ))
                );
    }

    @Override
    public Content create(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Имя уч. записи должно быть заполнено")
                    ));
        } else {
            Set<RoleGrantedAuthority> rolesSet = new HashSet<>();
            rolesSet.add(roleRepository.findById(userDto.getRoleId()).get());
            UserDetail userDetail = service.add(userDto.getPublicName(), userDto.getUsername(), userDto.getPassword(), rolesSet);
            return getContentView(userDetail.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Пользователь успешно добавлен")
                    ));
        }
    }

    @Override
    public Content delete(long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id).get())) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Пользователь удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Ошибка удаления Пользователя");
        }
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(getBaseManagement())
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    @Override
    public Content getContentView(long id) {
        Optional<UserDetail> userDetailOpt = service.getById(id);
        if (!userDetailOpt.isPresent()) {
            Notification notification = new Notification();
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Пользователь отсутствует");
            return new Content().setNotifications(List.of(notification));
        }
        UserDetail userDetail = userDetailOpt.get();
        return new Content()
                .setPageName(PAGE_NAME)
                .setManagement(List.of(
                        new Button().setTitle("Назад")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/users")
                                ),
                        new Button().setTitle("Редактировать")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/users/" + userDetail.getId() + "/edit")
                                ),
                        new Button().setTitle("Удалить")
                                .setPosition(3)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/users/" + userDetail.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя уч. записи (login)")
                                .setName("username")
                                .setValue(userDetail.getUsername()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя пользователя")
                                .setName("public_name")
                                .setValue(userDetail.getPublicName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Роли")
                                .setName("roles")
                                .setValue(String.join(", ", userDetail.getRoles().stream().map(RoleGrantedAuthority::getRole).toList()))
                ));
    }

    private List<Button> getBaseManagement() {
        return List.of(
                new Button().setTitle("Добавить запись")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/users/add")
                        )
        );
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (UserDetail userDetail : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/users/" + userDetail.getId())
                    )
                    .setColumns(List.of(
                            userDetail.getUsername(),
                            userDetail.getPublicName() == null ? "" : userDetail.getPublicName(),
                            userDetail.getRoles() == null ? "" : String.join(", ", userDetail.getRoles().stream().map(RoleGrantedAuthority::getRole).toList())
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Имя уч. записи", "Имя пользователя", "Роли"))
                .setRows(rows);
    }

    private ValueItem toValueItem(RoleGrantedAuthority roleGrantedAuthority) {
        ValueItem valueItem = new ValueItem();
        valueItem.setId(roleGrantedAuthority.getId());
        valueItem.setValue(roleGrantedAuthority.getRole());
        return valueItem;
    }

    private void addMenu(MenuRepository menuRepository) {
        if (menuRepository.findByLink("/users").size() == 0) {
            Menu menu = new Menu().setTitle(PAGE_NAME)
                    .setPosition(4)
                    .setMethod(HttpMethod.GET.name())
                    .setLink("/users")
                    .setAlt(true);
            menuRepository.save(menu);
        }
    }
}