package ru.rncb.dpec.domain.entity.dp.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import ru.rncb.dpec.domain.entity.dp.RequestedDocuments;
import ru.rncb.dpec.domain.entity.dp.SysResponse;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "document_type")
@org.hibernate.annotations.Table(comment = "Типы документов пользователя в ЕСИА (Цифровой профиль)", appliesTo = "document_type")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_type_seq")
    private long id;

    @Comment("Мнемоника типа документа")
    @Column(length = 2000)
    private String mnemonic;

    @Comment("Наименование документа")
    @Column(length = 2000)
    private String name;

    @Comment("Источник данных (ведомство предоставляющее документ)")
    @Column(length = 2000)
    private String source;

    @Comment("Тип файла запрашиваемого документа. Загалавными буквами XML или PDF")
    @Column(name = "file_type", length = 10)
    private String fileType;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "scope_id")
    private Scope scope;

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    private List<RequestedDocuments> requestedDocuments = new ArrayList<>();

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    private List<SysResponse> sysResponseList = new ArrayList<>();
}
