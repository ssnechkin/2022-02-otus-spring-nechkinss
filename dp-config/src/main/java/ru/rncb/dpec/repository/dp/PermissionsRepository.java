package ru.rncb.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.dp.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
}
