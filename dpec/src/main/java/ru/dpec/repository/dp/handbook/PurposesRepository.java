package ru.dpec.repository.dp.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.handbook.Purposes;

public interface PurposesRepository extends JpaRepository<Purposes, Long> {
}
