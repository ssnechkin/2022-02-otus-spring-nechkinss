package ru.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.Systems;

public interface SystemsRepository extends JpaRepository<Systems, Long> {
}
