package ru.rncb.dpec.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.security.RoleGrantedAuthority;

public interface RoleRepository extends JpaRepository<RoleGrantedAuthority, Long> {

    RoleGrantedAuthority findByRole(String role);
}
