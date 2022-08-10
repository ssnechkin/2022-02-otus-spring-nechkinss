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
@Entity(name = "purposes")
@org.hibernate.annotations.Table(comment = "Цели запроса согласия", appliesTo = "purposes")
public class Purposes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purposes_seq")
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
            joinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "purpose_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))}
    )
    private List<Permissions> permissionsList = new ArrayList<>();
}
