package ru.rncb.dpec.domain.entity.dp;

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
@Entity(name = "sys_permissions_map")
@org.hibernate.annotations.Table(comment = "Запрашиваемые согласия системой у клиента", appliesTo = "sys_permissions_map")
public class SysPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_permissions_map_seq")
    private long id;

    @Comment("Согласие по умолчанию. 1-использовать по умолчанию")
    @Column(name = "is_default")
    private long isDefault;

    @Comment("Проверяемое значение переданное в URL-параметре")
    @Column(length = 4000)
    private String comparing;

    @Comment("Наименование организации или ФИО ответственного сотрудника. Будет отображаться в личном кабинете у согласий")
    @Column(length = 2000)
    private String responsibleobject;

    @Comment("Время жизни согласия в минутах")
    private long expire;

    @Comment("Наименование списка полей, возвращаемых сервисов")
    @Column(name = "requested_documents_list_name", length = 500)
    private String requestedDocumentsListName;

    @Comment("Дата добавления записи")
    @Column(name = "date_create")
    private Timestamp dateCreate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "system_id")
    private Systems systems;

    @ManyToOne
    @JoinColumn(name = "permissions_id")
    private Permissions permissions;

    @OneToMany(mappedBy = "sysPermissions", orphanRemoval = true)
    private List<SysResponse> sysResponseList = new ArrayList<>();
}
