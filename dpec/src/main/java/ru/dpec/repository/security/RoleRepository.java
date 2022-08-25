package ru.dpec.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.security.RoleGrantedAuthority;

public interface RoleRepository extends JpaRepository<RoleGrantedAuthority, Long> {

    RoleGrantedAuthority findByRole(String role);
}
