package ru.rncb.dpec.domain.entity.dp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import ru.rncb.dpec.domain.entity.dp.handbook.Actions;
import ru.rncb.dpec.domain.entity.dp.handbook.Purposes;
import ru.rncb.dpec.domain.entity.dp.handbook.Scope;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "permissions")
@org.hibernate.annotations.Table(
        comment = "Согласия. Группа областей доступа к документам клиента в ЕСИА",
        appliesTo = "permissions")
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_seq")
    @SequenceGenerator(name = "permissions_seq", allocationSize = 1)
    private long id;

    @Comment("Мнемоника типа согласия")
    @Column(length = 2000)
    private String mnemonic;

    @Comment("Наименование типа согласия")
    @Column(length = 2000)
    private String name;

    @Comment("Наименование организации или ФИО ответственного сотрудника. " +
            "Будет отображаться в личном кабинете у согласий")
    @Column(length = 2000)
    private String responsibleobject = "Организация/ФИО ответственного представителя";

    @Comment("Время жизни согласия в минутах")
    private long expire = 180;

    @Comment("Описание согласия")
    @Column(length = 2000)
    private String description;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany(mappedBy = "permissionsList", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    private List<Scope> scopeList = new ArrayList<>();

    @ManyToMany(mappedBy = "permissionsList", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    private List<Purposes> purposesList = new ArrayList<>();

    @ManyToMany(mappedBy = "permissionsList", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    private List<Actions> actionsList = new ArrayList<>();
}
