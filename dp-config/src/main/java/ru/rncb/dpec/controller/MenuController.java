package ru.rncb.dpec.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rncb.dpec.domain.entity.Menu;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;
import ru.rncb.dpec.domain.entity.security.UserDetail;
import ru.rncb.dpec.domain.entity.security.acl.AclClass;
import ru.rncb.dpec.domain.entity.security.acl.AclEntry;
import ru.rncb.dpec.domain.entity.security.acl.AclObjectIdentity;
import ru.rncb.dpec.domain.entity.security.acl.AclSid;
import ru.rncb.dpec.dto.out.Content;
import ru.rncb.dpec.dto.out.content.Button;
import ru.rncb.dpec.dto.out.content.Link;
import ru.rncb.dpec.dto.out.content.Notification;
import ru.rncb.dpec.dto.out.content.TopRight;
import ru.rncb.dpec.dto.out.enums.NotificationType;
import ru.rncb.dpec.repository.MenuRepository;
import ru.rncb.dpec.repository.security.acl.AclClassRepository;
import ru.rncb.dpec.repository.security.acl.AclEntryRepository;
import ru.rncb.dpec.repository.security.acl.AclObjectIdentityRepository;
import ru.rncb.dpec.repository.security.acl.AclSidRepository;
import ru.rncb.dpec.security.SecurityAclService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MenuController {

    private final MenuRepository menuRepository;
    private final SecurityAclService securityAclService;
    private final AclClassRepository aclClassRepository;
    private final AclEntryRepository aclEntryRepository;
    private final AclObjectIdentityRepository aclObjectIdentityRepository;
    private final AclSidRepository aclSidRepository;

    public MenuController(MenuRepository menuRepository, SecurityAclService securityAclService, AclObjectIdentityRepository aclObjectIdentityRepository, AclClassRepository aclClassRepository, AclEntryRepository aclEntryRepository, AclEntryRepository aclEntryRepository1, AclSidRepository aclSidRepository) {
        this.menuRepository = menuRepository;
        this.securityAclService = securityAclService;
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
        this.aclClassRepository = aclClassRepository;
        this.aclEntryRepository = aclEntryRepository1;
        this.aclSidRepository = aclSidRepository;
        addAcl();
    }

    @GetMapping("/menu")
    @HystrixCommand(commandKey = "getFallKey", fallbackMethod = "fallback")
    public Content getMenu(@AuthenticationPrincipal UserDetail userDetail) {
        //addMenu();
        Content content = new Content();
        TopRight topRight = new TopRight();
        Button logout = new Button();
        logout.setTitle("Выйти");
        logout.setLink(new Link(HttpMethod.POST, "/logout"));
        topRight.setButtons(List.of(logout));
        topRight.setText(userDetail != null ? userDetail.getPublicName() : "");
        content.setTopRight(topRight);
        content.setButtons(getAssembledMenu(userDetail));
        content.setManagement(List.of());
        content.setPageName("Конфигуратор интеграционных сервисов цифрового профиля ЕСИА");
        content.setFields(List.of());
        return content;
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private void addAcl() {
        if (aclClassRepository.findAll().size() == 0) {
            aclClassRepository.save(new AclClass(1, Menu.class.getName()));

            aclSidRepository.save(new AclSid(1, false, "ROLE_ADMIN"));

            aclObjectIdentityRepository.save(new AclObjectIdentity(1, 1, "1", null, 1, true));

            aclEntryRepository.save(new AclEntry(1, 1, 1, 1, 7, false, false, false));
        }
    }

    private List<Button> getAssembledMenu(UserDetail userDetail) {
        List<Button> buttons = new LinkedList<>();
        for (Menu menu : getAllMenu()) {
            buttons.add(new Button()
                    .setPosition(menu.getPosition())
                    .setTitle(menu.getTitle())
                    .setAlt(true)
                    .setLink(new Link()
                            .setValue(menu.getLink())
                            .setMethod(HttpMethod.valueOf(menu.getMethod()))
                    )
            );
            /*try {
                ObjectIdentityImpl oid = new ObjectIdentityImpl(Menu.class, menu.getId());
                Acl acl = securityAclService.getJdbcMutableAclService().readAclById(oid);
                List<Permission> permissions = List.of(BasePermission.READ);
                List<Sid> sids = new ArrayList<>();
                for (RoleGrantedAuthority role : userDetail.getRoles()) {
                    sids.add(new GrantedAuthoritySid(role.getRole()));
                }
                try {
                    if (acl.isGranted(permissions, sids, true) && acl.isSidLoaded(sids)) {
                        buttons.add(new Button()
                                .setPosition(menu.getPosition())
                                .setTitle(menu.getTitle())
                                .setAlt(true)
                                .setLink(new Link()
                                        .setValue(menu.getLink())
                                        .setMethod(HttpMethod.valueOf(menu.getMethod()))
                                )
                        );
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        Comparator<Button> comparator = (o1, o2) -> o1.getPosition() >= o2.getPosition() ? -1 : 0;
        buttons.sort(comparator);
        return buttons;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }
}
