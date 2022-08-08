package ru.rncb.dpec.repository.dp.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;

public interface ActionsRepository extends JpaRepository<Actions, Long> {
}
