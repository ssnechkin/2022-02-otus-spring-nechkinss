package ru.rncb.dpec.service.dp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;
import ru.rncb.dpec.repository.dp.RequestedDocumentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RequestedDocumentsServiceImpl implements RequestedDocumentsService {

    private final RequestedDocumentsRepository repository;

    public RequestedDocumentsServiceImpl(RequestedDocumentsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public RequestedDocuments add(DocumentType documentType, String apiVersion, String fileType, String listName, boolean extended) {
        RequestedDocuments requestedDocuments = new RequestedDocuments();
        requestedDocuments.setDocumentType(documentType);
        requestedDocuments.setApiVersion(apiVersion);
        requestedDocuments.setExtended(extended);
        requestedDocuments.setFileType(fileType);
        requestedDocuments.setListName(listName);
        return repository.save(requestedDocuments);
    }

    @Override
    public RequestedDocuments getById(long id) {
        Optional<RequestedDocuments> requestedDocuments = repository.findById(id);
        return requestedDocuments.orElse(null);
    }

    @Override
    public List<RequestedDocuments> getAll() {
        return repository.findAll();
    }

    @Override
    public RequestedDocuments edit(RequestedDocuments requestedDocuments, DocumentType documentType, String apiVersion, String fileType, String listName, boolean extended) {
        requestedDocuments.setDocumentType(documentType);
        requestedDocuments.setApiVersion(apiVersion);
        requestedDocuments.setExtended(extended);
        requestedDocuments.setFileType(fileType);
        requestedDocuments.setListName(listName);
        return repository.save(requestedDocuments);
    }

    @Override
    public boolean delete(RequestedDocuments requestedDocuments) {
        repository.delete(requestedDocuments);
        return true;
    }
}