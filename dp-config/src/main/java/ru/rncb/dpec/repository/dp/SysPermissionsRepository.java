package ru.rncb.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.dp.SysPermissions;

public interface SysPermissionsRepository extends JpaRepository<SysPermissions, Long> {
}