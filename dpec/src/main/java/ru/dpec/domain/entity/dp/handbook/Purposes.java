package ru.dpec.domain.entity.dp.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import ru.dpec.domain.entity.dp.Permissions;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "purposes")
@org.hibernate.annotations.Table(comment = "Цели запроса согласия", appliesTo = "purposes")
public class Purposes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purposes_seq")
    @SequenceGenerator(name = "purposes_seq", allocationSize = 1)
    private long id;

    @Comment("Мнемоника цели")
    @Column(length = 2000)
    private String mnemonic;

    @Comment("Наименование цели")
    @Column(length = 2000)
    private String name;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    @JoinTable(name = "permissions_purpose",
            joinColumns = {@JoinColumn(name = "purposes_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id")}
    )
    private List<Permissions> permissionsList = new ArrayList<>();
}
