package ru.otus.homework.repository.security.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.security.acl.AclEntry;


public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
}
