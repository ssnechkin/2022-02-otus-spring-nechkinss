package ru.rncb.dpec.domain.entity.dp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import ru.rncb.dpec.domain.entity.dp.handbook.DocumentType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "requested_documents")
@org.hibernate.annotations.Table(comment = "Набор запрашиваемых документов из ЕСИА", appliesTo = "requested_documents")
public class RequestedDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requested_documents_id_seq")
    @SequenceGenerator(name = "requested_documents_id_seq", allocationSize = 1)
    private long id;

    @Comment("Наименование листа запрашиваемых документов из ЕСИА")
    @Column(name = "list_name", length = 500)
    private String listName = "requested_documents_list";

    @Comment("Версия API. Текущая версия: v1. Для документа " +
            "(Сведения о состоянии индивидуального страхового счета застрахованного лица) " +
            "(ILS_PFR) доступна вторая версия (v2) API")
    @Column(name = "api_version", length = 10)
    private String apiVersion = "v1";

    @Comment("Тип файла запрашиваемого документа. Загалавными буквами XML или PDF")
    @Column(name = "file_type", length = 10)
    private String fileType;

    @Comment("Признак расширенной модели данных (может быть указан только в отношении свидетельств " +
            "о браке и разводе). Если указан extended=true, то тогда в ответе вернется информация " +
            "о соответствующей записи акта гражданского состояния (при наличии)")
    private boolean extended = false;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
}
