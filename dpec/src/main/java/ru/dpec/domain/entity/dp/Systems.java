package ru.dpec.domain.entity.dp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "systems")
@org.hibernate.annotations.Table(comment = "Системы запрашивающие согласия", appliesTo = "systems")
public class Systems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systems_seq")
    @SequenceGenerator(name = "systems_seq", allocationSize = 1)
    private long id;

    @Comment("Наименование системы")
    @Column(length = 4000)
    private String name;

    @Comment("Описание системы")
    @Column(length = 4000)
    private String description;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @OneToMany(mappedBy = "systems", orphanRemoval = true)
    private List<SysPermissions> sysPermissionsList = new ArrayList<>();
}
