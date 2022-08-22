package ru.dpec.service.dp.documents;

import ru.dpec.domain.entity.dp.RequestedDocuments;
import ru.dpec.domain.entity.dp.handbook.DocumentType;

import java.util.List;

public interface RequestedDocumentsService {

    RequestedDocuments add(DocumentType documentType, String apiVersion, String fileType,
                           String listName, boolean extended);

    RequestedDocuments getById(long id);

    List<RequestedDocuments> getAll();

    List<RequestedDocuments> getAllByListName(String listName);

    RequestedDocuments edit(RequestedDocuments requestedDocuments, DocumentType documentType,
                            String apiVersion, String fileType, String listName, boolean extended);

    boolean delete(RequestedDocuments requestedDocuments);
}
