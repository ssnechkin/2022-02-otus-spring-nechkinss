package ru.otus.homework.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.security.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByUsername(String username);
}
