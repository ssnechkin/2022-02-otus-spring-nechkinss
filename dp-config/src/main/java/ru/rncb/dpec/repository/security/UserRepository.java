package ru.rncb.dpec.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.security.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByUsername(String username);
}
