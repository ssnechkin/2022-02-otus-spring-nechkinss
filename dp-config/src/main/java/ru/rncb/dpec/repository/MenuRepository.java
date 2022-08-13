package ru.rncb.dpec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
