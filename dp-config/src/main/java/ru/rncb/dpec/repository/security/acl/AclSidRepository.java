package ru.rncb.dpec.repository.security.acl;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.security.acl.AclSid;

public interface AclSidRepository extends JpaRepository<AclSid, Long> {
}
