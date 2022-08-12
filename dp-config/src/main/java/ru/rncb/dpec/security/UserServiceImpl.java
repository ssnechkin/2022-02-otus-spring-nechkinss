package ru.rncb.dpec.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;
import ru.rncb.dpec.domain.entity.security.UserDetail;
import ru.rncb.dpec.repository.security.RoleRepository;
import ru.rncb.dpec.repository.security.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = userRepository.findByUsername(username);
        if (userDetail == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetail;
    }

    @Override
    public UserDetail add(String publicName, String username, String password, Set<RoleGrantedAuthority> roleSet) {
        return createUser(publicName, username, password, roleSet);
    }

    @Override
    public UserDetail edit(UserDetail userDetail, String publicName, String username, String password, Set<RoleGrantedAuthority> roleSet) {
        return saveUserDetail(userDetail, publicName, username, password, roleSet);
    }

    @Override
    public Optional<UserDetail> getById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean delete(UserDetail userDetail) {
        userRepository.delete(userDetail);
        return true;
    }

    @Override
    public List<UserDetail> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Set<RoleGrantedAuthority> createRoles(List<String> roles) {
        Set<RoleGrantedAuthority> rolesSet = new HashSet<>();
        for (String role : roles) {
            RoleGrantedAuthority roleGrantedAuthority = roleRepository.findByRole(role);
            if (roleGrantedAuthority == null) {
                roleGrantedAuthority = new RoleGrantedAuthority();
                roleGrantedAuthority.setRole(role);
                roleRepository.save(roleGrantedAuthority);
            }
            rolesSet.add(roleGrantedAuthority);
        }
        return rolesSet;
    }

    private UserDetail createUser(String publicName, String username, String password, Set<RoleGrantedAuthority> rolesSet) {
        UserDetail userDetail = userRepository.findByUsername(username);
        if (userDetail == null) {
            userDetail = new UserDetail();
            saveUserDetail(userDetail, publicName, username, password, rolesSet);
            return userDetail;
        }
        return userDetail;
    }

    private UserDetail saveUserDetail(UserDetail userDetail, String publicName, String username, String password, Set<RoleGrantedAuthority> rolesSet) {
        String pass = password;
        if (userDetail.getPassword() == null || !userDetail.getPassword().equals(pass)) {
            pass = new BCryptPasswordEncoder().encode(password);
        }
        userDetail.setUsername(username);
        userDetail.setPublicName(publicName);
        userDetail.setPassword(pass);
        userDetail.setRoles(rolesSet);
        return userRepository.save(userDetail);
    }
}
