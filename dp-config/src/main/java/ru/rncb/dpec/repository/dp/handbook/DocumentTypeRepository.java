package ru.rncb.dpec.repository.dp.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
