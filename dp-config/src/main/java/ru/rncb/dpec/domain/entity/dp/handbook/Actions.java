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
@Entity(name = "actions")
@org.hibernate.annotations.Table(comment = "Действия запроса согласия", appliesTo = "actions")
public class Actions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actions_seq")
    @SequenceGenerator(name = "actions_seq", allocationSize = 1)
    private long id;

    @Comment("Действие запроса согласия")
    @Column(length = 2000)
    private String mnemonic;

    @Comment("Наименование действия")
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
    @JoinTable(name = "permissions_action",
            joinColumns = {@JoinColumn(name = "actions_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id")}
    )
    private List<Permissions> permissionsList = new ArrayList<>();
}
