package ru.rncb.dpec.service.dp.handbook;

import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;

import java.util.List;

public interface DocumentTypeService {

    DocumentType add(String mnemonic, String name, Scope scope, String source);

    DocumentType getById(long id);

    List<DocumentType> getAll();

    DocumentType edit(DocumentType documentType, String mnemonic, String name, Scope scope, String source);

    boolean delete(DocumentType documentType);
}
