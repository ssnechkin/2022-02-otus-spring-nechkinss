/*
INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_ADMIN'),
(2, false, 'ROLE_EDITOR'),
(3, false, 'ROLE_VISITOR');

INSERT INTO public.acl_class(id, class)	VALUES
(1, 'ru.rncb.dpec.domain.entity.Menu');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_ADMIN'),
(2, false, 'ROLE_EDITOR'),
(3, false, 'ROLE_VISITOR');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 4, NULL, 1, false);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, false, false),
(2, 1, 2, 2, 1, false, false, false),
(3, 1, 3, 3, 7, true, false, false);
*/
