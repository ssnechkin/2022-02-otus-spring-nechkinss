package ru.otus.homework.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.entity.security.RoleGrantedAuthority;
import ru.otus.homework.domain.entity.security.UserDetail;
import ru.otus.homework.repository.security.RoleRepository;
import ru.otus.homework.repository.security.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        createUsers(roleRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = userRepository.findByUsername(username);
        if (userDetail == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetail;
    }

    private void createUsers(RoleRepository roleRepository) {
        creaateUser(roleRepository, "Администратор", "admin", "password", List.of("ROLE_ADMIN"));
        creaateUser(roleRepository, "Редактор", "editor", "123", List.of("ROLE_EDITOR"));
        creaateUser(roleRepository, "Посетитель", "visitor", "123", List.of("ROLE_VISITOR"));
    }

    private void creaateUser(RoleRepository roleRepository, String publicName, String username, String password, List<String> roles) {
        UserDetail userDetail = userRepository.findByUsername(username);
        if (userDetail == null) {
            Set<RoleGrantedAuthority> rolesSet = new HashSet<>();
            for (String role : roles) {
                RoleGrantedAuthority roleGrantedAuthority = roleRepository.findByName(role);
                if (roleGrantedAuthority == null) {
                    roleGrantedAuthority = new RoleGrantedAuthority();
                    roleGrantedAuthority.setName(role);
                    roleRepository.save(roleGrantedAuthority);
                }
                rolesSet.add(roleGrantedAuthority);
            }
            userDetail = new UserDetail();
            userDetail.setUsername(username);
            userDetail.setPublicName(publicName);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userDetail.setPassword(bCryptPasswordEncoder.encode(password));
            userDetail.setRoles(rolesSet);
            userRepository.save(userDetail);
        }
    }
}
