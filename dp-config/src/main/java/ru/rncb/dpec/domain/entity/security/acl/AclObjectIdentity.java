package ru.rncb.dpec.domain.entity.security.acl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AclObjectIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "object_id_class")
    private long objectIdClass;

    @Column(name = "object_id_identity")
    private String objectIdIdentity;

    @Column(name = "parent_object")
    private Long parentObject;

    @Column(name = "owner_sid")
    private long ownerSid;

    @Column(name = "entries_inheriting")
    private boolean entriesInheriting;
}
