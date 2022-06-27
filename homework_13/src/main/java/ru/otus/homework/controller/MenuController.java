package ru.otus.homework.controller;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.domain.entity.Menu;
import ru.otus.homework.domain.entity.security.UserDetail;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;
import ru.otus.homework.dto.out.content.TopRight;
import ru.otus.homework.repository.MenuRepository;
import ru.otus.homework.security.SecurityAclService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MenuController {


    private final MenuRepository menuRepository;
    private final SecurityAclService securityAclService;
    private Boolean isAdd = false;

    public MenuController(MenuRepository menuRepository, SecurityAclService securityAclService) {
        this.menuRepository = menuRepository;
        this.securityAclService = securityAclService;
    }

    @GetMapping("/menu")
    public Content getMenu(@AuthenticationPrincipal UserDetail userDetail) {
        addMenu();
        Content content = new Content();
        TopRight topRight = new TopRight();
        Button logout = new Button();
        logout.setTitle("Выйти");
        logout.setLink(new Link(HttpMethod.POST, "/logout"));
        topRight.setButtons(List.of(logout));
        topRight.setText(userDetail != null ? userDetail.getPublicName() : "");
        content.setTopRight(topRight);
        content.setButtons(getMenu());
        content.setManagement(List.of());
        content.setPageName("Добро пожаловать в библиотеку книг");
        content.setFields(List.of());
        return content;
    }


    private void addMenu() {
        if (!isAdd) {
            isAdd = true;

            Menu author = menuRepository.findByLink("/author").get(0);
            Menu genre = menuRepository.findByLink("/genre").get(0);
            Menu book = menuRepository.findByLink("/book").get(0);

            securityAclService.addSecurityUserRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", true);
            /*securityAclService.addSecurityRoleRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", true);
            securityAclService.addSecurityRoleRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_EDITOR", true);
            securityAclService.addSecurityRoleRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", false);
            securityAclService.addSecurityRoleRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_EDITOR", false);
            securityAclService.addSecurityUserRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", true);
            securityAclService.addSecurityUserRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_EDITOR", true);
            securityAclService.addSecurityUserRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_ADMIN", false);
            securityAclService.addSecurityUserRight(author.getClass(), author.getId(), BasePermission.READ, "ROLE_EDITOR", false);
  */      }
    }

    private List<Button> getMenu() {

        List<Button> buttons = new LinkedList<>();
        List<Menu> menus = getAllMenu();
        System.out.println(menus.size());
        for (Menu menu : menus) {
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
        /*for (Menu menu : menus) {
            ObjectIdentityImpl oid = new ObjectIdentityImpl(Menu.class, menu.getId());
            Acl acl = securityAclService.getAclService().readAclById(oid);

            List<Permission> permissions = Arrays.asList(BasePermission.READ);
            //List<Sid> sids = Arrays.asList((Sid) new PrincipalSid("ROLE_ADMIN"));//acl_sid.principal=true
            List<Sid> sids = Arrays.asList((Sid) new GrantedAuthoritySid("ROLE_ADMIN"));//acl_sid.principal=false
            //AclSid aclSid = roleRepository.findBySid("ROLE_ADMIN");
            //List<Sid> sids = Arrays.asList((Sid) aclSid);

            try {
                for (AccessControlEntry ace : acl.getEntries()) {
                    System.out.println("======");
                    System.out.println("1" + (ace.getPermission().getMask() == permissions.get(0).getMask()));
                    System.out.println("2" + (ace.getSid().equals(sids.get(0))));
                    System.out.println("3" + (ace.isGranting()));
                    System.out.println("3" + (ace.getSid().getClass() + " " + sids.get(0).getClass()));
                }
                if (!acl.isGranted(permissions, sids, false)) {
                    buttons.add(new Button()
                            .setPosition(menu.getPosition())
                            .setTitle(menu.getTitle())
                            .setAlt(true)
                            .setLink(new Link()
                                    .setValue(menu.getLink())
                                    .setMethod(HttpMethod.valueOf(menu.getMethod()))
                            )
                    );
                    System.out.println("+++++++++++++++");
                } else {
                    System.out.println("---------------");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
        Comparator<Button> comparator = (o1, o2) -> o1.getPosition() >= o2.getPosition() ? -1 : 0;
        buttons.sort(comparator);
        return buttons;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    private List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }
}
