package ru.rncb.dpec.repository.security.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.security.acl.AclEntry;


public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
}