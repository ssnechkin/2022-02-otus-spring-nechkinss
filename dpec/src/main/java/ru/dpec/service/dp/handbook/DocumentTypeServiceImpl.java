package ru.dpec.service.dp.handbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpec.domain.entity.dp.handbook.DocumentType;
import ru.dpec.domain.entity.dp.handbook.Scope;
import ru.dpec.repository.dp.handbook.DocumentTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository repository;

    public DocumentTypeServiceImpl(DocumentTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public DocumentType add(String mnemonic, String name, Scope scope, String source) {
        DocumentType documentType = new DocumentType();
        documentType.setMnemonic(mnemonic);
        documentType.setName(name);
        documentType.setScope(scope);
        documentType.setSource(source);
        return repository.save(documentType);
    }

    @Override
    public DocumentType getById(long id) {
        Optional<DocumentType> documentType = repository.findById(id);
        return documentType.orElse(null);
    }

    @Override
    public List<DocumentType> getAll() {
        return repository.findAll();
    }

    @Override
    public DocumentType edit(DocumentType documentType, String mnemonic, String name, Scope scope, String source) {
        documentType.setMnemonic(mnemonic);
        documentType.setName(name);
        documentType.setScope(scope);
        documentType.setSource(source);
        return repository.save(documentType);
    }

    @Override
    public boolean delete(DocumentType documentType) {
        repository.delete(documentType);
        return true;
    }
}