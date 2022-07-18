package ru.otus.homework.repository.security.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.security.acl.AclSid;

public interface AclSidRepository extends JpaRepository<AclSid, Long> {
}
