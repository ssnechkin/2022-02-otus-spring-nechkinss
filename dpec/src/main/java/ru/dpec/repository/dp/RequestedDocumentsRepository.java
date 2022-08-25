package ru.dpec.repository.dp;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dpec.domain.entity.dp.RequestedDocuments;

import java.util.List;

public interface RequestedDocumentsRepository extends JpaRepository<RequestedDocuments, Long> {
    List<RequestedDocuments> findByListName(String listName);
}
