package ru.rncb.dpec.domain.entity.dp.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import ru.rncb.dpec.domain.entity.dp.Permissions;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "scope")
@org.hibernate.annotations.Table(comment = "Области доступа к данным клиента", appliesTo = "scope")
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scope_seq")
    private long id;

    @Comment("Наименование (Мнемоника области доступа)")
    @Column(length = 2000)
    private String name;

    @Comment("Описание")
    @Column(length = 2000)
    private String description;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    @JoinTable(name = "permissions_scope",
            joinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "scope_id", referencedColumnName = "id")}
    )
    private List<Permissions> permissionsList = new ArrayList<>();

    @OneToMany(mappedBy = "scope", orphanRemoval = true)
    private List<DocumentType> documentTypes = new ArrayList<>();
}
