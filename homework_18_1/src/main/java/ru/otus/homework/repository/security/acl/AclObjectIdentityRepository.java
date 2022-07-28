package ru.otus.homework.repository.security.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.security.acl.AclObjectIdentity;

public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {
}
