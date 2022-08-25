DELETE FROM public.acl_entry;
DELETE FROM public.acl_object_identity;
DELETE FROM public.acl_class;
DELETE FROM public.acl_sid;
DELETE FROM public.menu;

INSERT INTO public.menu(id, position, title, alt, method, link) VALUES
(1, 4, 'Пользователи', true, 'GET', '/users'),
(2, 3, 'Справочники', true, 'GET', '/handbook'),
(3, 2, 'Согласия', true, 'GET', '/permissions'),
(4, 1, 'Системы', true, 'GET', '/systems');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_ADMIN'),
(2, false, 'ROLE_EDITOR'),
(3, false, 'ROLE_VISITOR');

INSERT INTO public.acl_class(id, class)	VALUES
(1, 'ru.dpec.domain.entity.Menu');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, false),
(2, 1, 2, NULL, 1, false),
(3, 1, 3, NULL, 1, false),
(4, 1, 4, NULL, 1, false);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, false, false),

(2, 2, 2, 1, 1, true, false, false),
(3, 2, 3, 2, 1, true, false, false),
(4, 2, 4, 3, 1, true, false, false),

(5, 3, 5, 1, 1, true, false, false),
(6, 3, 6, 2, 1, true, false, false),
(7, 3, 7, 3, 1, true, false, false),

(8, 4, 8, 1, 1, true, false, false),
(9, 4, 9, 2, 1, true, false, false),
(10, 4, 10, 3, 1, true, false, false);