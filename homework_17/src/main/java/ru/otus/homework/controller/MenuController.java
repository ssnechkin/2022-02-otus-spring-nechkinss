package ru.otus.homework.controller;

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
import ru.otus.homework.domain.entity.Menu;
import ru.otus.homework.domain.entity.security.RoleGrantedAuthority;
import ru.otus.homework.domain.entity.security.UserDetail;
import ru.otus.homework.domain.entity.security.acl.AclClass;
import ru.otus.homework.domain.entity.security.acl.AclEntry;
import ru.otus.homework.domain.entity.security.acl.AclObjectIdentity;
import ru.otus.homework.domain.entity.security.acl.AclSid;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;
import ru.otus.homework.dto.out.content.TopRight;
import ru.otus.homework.repository.MenuRepository;
import ru.otus.homework.repository.security.acl.AclClassRepository;
import ru.otus.homework.repository.security.acl.AclEntryRepository;
import ru.otus.homework.repository.security.acl.AclObjectIdentityRepository;
import ru.otus.homework.repository.security.acl.AclSidRepository;
import ru.otus.homework.security.SecurityAclService;

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
        content.setPageName("Добро пожаловать в библиотеку книг");
        content.setFields(List.of());
        return content;
    }

    private void addAcl() {
        if (aclClassRepository.findAll().size() == 0) {
            aclClassRepository.save(new AclClass(1, Menu.class.getName()));

            aclSidRepository.save(new AclSid(1, false, "ROLE_ADMIN"));
            aclSidRepository.save(new AclSid(2, false, "'ROLE_EDITOR'"));
            aclSidRepository.save(new AclSid(3, false, "'ROLE_VISITOR'"));
            aclSidRepository.save(new AclSid(4, false, "'visitor'"));
            aclSidRepository.save(new AclSid(5, false, "'editor'"));

            aclObjectIdentityRepository.save(new AclObjectIdentity(1, 1, "1", null, 2, true));
            aclObjectIdentityRepository.save(new AclObjectIdentity(2, 1, "2", null, 2, true));
            aclObjectIdentityRepository.save(new AclObjectIdentity(3, 1, "3", null, 2, true));

            aclEntryRepository.save(new AclEntry(1, 1, 1, 1, 4, false, false, false));
            aclEntryRepository.save(new AclEntry(2, 2, 2, 1, 3, false, false, false));
            aclEntryRepository.save(new AclEntry(3, 3, 3, 1, 4, false, false, false));
            aclEntryRepository.save(new AclEntry(4, 1, 4, 3, 1, false, false, false));
            aclEntryRepository.save(new AclEntry(5, 1, 5, 2, 5, false, false, false));
            aclEntryRepository.save(new AclEntry(6, 2, 6, 2, 6, false, false, false));
            aclEntryRepository.save(new AclEntry(7, 3, 7, 2, 7, false, false, false));
        }
    }

    private void addMenu() {
        Menu author = menuRepository.findByLink("/author").get(0);
        Menu genre = menuRepository.findByLink("/genre").get(0);
        Menu book = menuRepository.findByLink("/book").get(0);
        addMenuInAcl(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", true);
        addMenuInAcl(author.getClass(), author.getId(), BasePermission.READ, "ROLE_EDITOR", true);
        addMenuInAcl(genre.getClass(), genre.getId(), BasePermission.READ, "ROLE_ADMIN", true);
        addMenuInAcl(genre.getClass(), genre.getId(), BasePermission.READ, "ROLE_EDITOR", true);
        addMenuInAcl(book.getClass(), book.getId(), BasePermission.READ, "ROLE_ADMIN", true);
        addMenuInAcl(book.getClass(), book.getId(), BasePermission.READ, "ROLE_EDITOR", true);
        addMenuInAcl(book.getClass(), book.getId(), BasePermission.READ, "ROLE_VISITOR", true);
    }

    private void addMenuInAcl(Class<?> clas, Serializable objectId, Permission permission, String role, boolean granting) {
        try {
            securityAclService.addSecurityRoleRight(clas, objectId, permission, role, granting);
        } catch (Exception ignore) {
        }
    }

    private List<Button> getAssembledMenu(UserDetail userDetail) {
        List<Button> buttons = new LinkedList<>();
        for (Menu menu : getAllMenu()) {
            try {
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
            }
        }
        Comparator<Button> comparator = (o1, o2) -> o1.getPosition() >= o2.getPosition() ? -1 : 0;
        buttons.sort(comparator);
        return buttons;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    private List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }
}
