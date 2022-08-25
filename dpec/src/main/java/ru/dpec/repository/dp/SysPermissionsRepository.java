package ru.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.SysPermissions;

public interface SysPermissionsRepository extends JpaRepository<SysPermissions, Long> {
}
