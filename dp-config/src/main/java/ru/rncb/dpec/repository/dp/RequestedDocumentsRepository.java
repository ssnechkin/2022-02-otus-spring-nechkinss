package ru.rncb.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;

public interface RequestedDocumentsRepository extends JpaRepository<RequestedDocuments, Long> {
}
