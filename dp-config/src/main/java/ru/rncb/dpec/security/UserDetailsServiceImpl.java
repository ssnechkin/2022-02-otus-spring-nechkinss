package ru.rncb.dpec.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
        createUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }

    private void createUsers() {
        userService.add("Администратор", "admin", "password", userService.createRoles(List.of("ROLE_ADMIN")));
        userService.add("Редактор", "editor", "123", userService.createRoles(List.of("ROLE_EDITOR")));
        userService.add("Посетитель", "visitor", "123", userService.createRoles(List.of("ROLE_VISITOR")));
    }
}
