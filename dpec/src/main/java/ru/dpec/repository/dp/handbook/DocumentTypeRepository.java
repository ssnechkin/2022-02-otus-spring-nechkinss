package ru.dpec.repository.dp.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.handbook.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
