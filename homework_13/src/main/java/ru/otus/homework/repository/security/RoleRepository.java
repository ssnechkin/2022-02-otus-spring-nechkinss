package ru.otus.homework.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.security.RoleGrantedAuthority;

public interface RoleRepository extends JpaRepository<RoleGrantedAuthority, Long> {

    RoleGrantedAuthority findByRole(String role);
}
