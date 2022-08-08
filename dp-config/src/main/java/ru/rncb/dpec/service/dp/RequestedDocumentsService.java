package ru.rncb.dpec.service.dp;

import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;

import java.util.List;

public interface RequestedDocumentsService {

    RequestedDocuments add(DocumentType documentType, String apiVersion, String fileType, String listName, boolean extended);

    RequestedDocuments getById(long id);

    List<RequestedDocuments> getAll();

    RequestedDocuments edit(RequestedDocuments requestedDocuments, DocumentType documentType, String apiVersion, String fileType, String listName, boolean extended);

    boolean delete(RequestedDocuments requestedDocuments);
}
