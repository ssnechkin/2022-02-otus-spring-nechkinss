package ru.otus.homework.domain.entity.security.acl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AclEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "acl_object_identity")
    private long aclObjectIdentity;

    @Column(name = "ace_order")
    private int ace_order;

    @Column(name = "sid")
    private long sid;

    @Column(name = "mask")
    private int mask;

    @Column(name = "granting")
    private boolean granting;

    @Column(name = "audit_success")
    private boolean auditSuccess;

    @Column(name = "audit_failure")
    private boolean auditFailure;
}
