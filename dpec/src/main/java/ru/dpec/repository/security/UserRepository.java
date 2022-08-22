package ru.dpec.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.security.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByUsername(String username);
}
