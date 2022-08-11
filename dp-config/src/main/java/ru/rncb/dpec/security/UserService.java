package ru.rncb.dpec.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;
import ru.rncb.dpec.domain.entity.security.UserDetail;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetail add(String publicName, String username, String password, Set<RoleGrantedAuthority> roleSet);

    UserDetail edit(UserDetail userDetail, String publicName, String username, String password, Set<RoleGrantedAuthority> roleSet);

    Optional<UserDetail> getById(long id);

    boolean delete(UserDetail userDetail);

    List<UserDetail> getAll();

    Set<RoleGrantedAuthority> createRoles(List<String> roles);
}
