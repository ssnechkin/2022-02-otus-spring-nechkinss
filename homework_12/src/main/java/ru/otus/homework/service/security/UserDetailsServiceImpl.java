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

import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        UserDetail userDetail = userRepository.findByUsername("admin");
        if (userDetail == null) {
            RoleGrantedAuthority roleGrantedAuthority = roleRepository.findByName("ROLE_ADMIN");
            if (roleGrantedAuthority == null) {
                roleGrantedAuthority = new RoleGrantedAuthority();
                roleGrantedAuthority.setName("ROLE_ADMIN");
                roleRepository.save(roleGrantedAuthority);
            }
            userDetail = new UserDetail();
            userDetail.setUsername("admin");
            userDetail.setPublicName("Администратор");
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userDetail.setPassword(bCryptPasswordEncoder.encode("password"));
            userDetail.setRoles(Set.of(roleGrantedAuthority));
            userRepository.save(userDetail);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = userRepository.findByUsername(username);
        if (userDetail == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetail;
    }
}
