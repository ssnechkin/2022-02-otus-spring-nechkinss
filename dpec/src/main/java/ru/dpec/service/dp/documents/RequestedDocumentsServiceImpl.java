package ru.dpec.service.dp.documents;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpec.domain.entity.dp.RequestedDocuments;
import ru.dpec.domain.entity.dp.handbook.DocumentType;
import ru.dpec.repository.dp.RequestedDocumentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RequestedDocumentsServiceImpl implements RequestedDocumentsService {

    private final RequestedDocumentsRepository repository;

    public RequestedDocumentsServiceImpl(RequestedDocumentsRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestedDocuments add(DocumentType documentType, String apiVersion, String fileType,
                                  String listName, boolean extended) {
        RequestedDocuments requestedDocuments = new RequestedDocuments();
        requestedDocuments.setDocumentType(documentType);
        requestedDocuments.setApiVersion(apiVersion == null || apiVersion.isEmpty() ? "v1" : apiVersion);
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
    public List<RequestedDocuments> getAllByListName(String listName) {
        return repository.findByListName(listName);
    }

    @Override
    public RequestedDocuments edit(RequestedDocuments requestedDocuments, DocumentType documentType,
                                   String apiVersion, String fileType, String listName, boolean extended) {
        requestedDocuments.setDocumentType(documentType);
        requestedDocuments.setApiVersion(apiVersion == null || apiVersion.isEmpty() ? "v1" : apiVersion);
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