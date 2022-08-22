package ru.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
}
