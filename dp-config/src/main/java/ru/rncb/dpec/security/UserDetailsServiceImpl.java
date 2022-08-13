package ru.rncb.dpec.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
        createRoles();
        createUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }

    private void createRoles() {
        if (userService.getByRole("ROLE_ADMIN") == null) {
            userService.createRoles(List.of("ROLE_ADMIN"));
        }
        if (userService.getByRole("ROLE_EDITOR") == null) {
            userService.createRoles(List.of("ROLE_EDITOR"));
        }
        if (userService.getByRole("ROLE_VISITOR") == null) {
            userService.createRoles(List.of("ROLE_VISITOR"));
        }
    }

    private void createUsers() {
        if (userService.getAll().size() == 0) {
            Set<RoleGrantedAuthority> roleSet;

            roleSet = new HashSet<>();
            roleSet.add(userService.getByRole("ROLE_ADMIN"));
            userService.add("Администратор", "admin", "password", roleSet);

            roleSet = new HashSet<>();
            roleSet.add(userService.getByRole("ROLE_EDITOR"));
            userService.add("Редактор", "editor", "123", roleSet);

            roleSet = new HashSet<>();
            roleSet.add(userService.getByRole("ROLE_VISITOR"));
            userService.add("Посетитель", "visitor", "123", roleSet);
        }
    }
}
