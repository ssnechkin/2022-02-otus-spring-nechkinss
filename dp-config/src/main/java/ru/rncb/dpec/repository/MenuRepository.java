package ru.rncb.dpec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByLink(String link);
}
