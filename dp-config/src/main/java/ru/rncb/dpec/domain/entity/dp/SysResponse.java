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
@Entity(name = "sys_response_map")
@org.hibernate.annotations.Table(comment = "Карта сопоставленя данных системы с согласиями (Для фильтрации полей json-ответа)", appliesTo = "sys_response_map")
public class SysResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_response_map_seq")
    @SequenceGenerator(name = "sys_response_map_seq", allocationSize = 1)
    private long id;

    @Comment("Значения ключа(json-путь к значению поля документа) для формирования ответа")
    @Column(name = "document_fact_key", length = 4000)
    private String documentFactKey;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "sys_permissions_map_id")
    private SysPermissions sysPermissions;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
}
