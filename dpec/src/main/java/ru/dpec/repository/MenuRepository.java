package ru.dpec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
