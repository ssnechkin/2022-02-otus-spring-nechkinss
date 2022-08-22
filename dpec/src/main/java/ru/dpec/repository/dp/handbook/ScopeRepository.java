package ru.dpec.repository.dp.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.handbook.Scope;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
}
