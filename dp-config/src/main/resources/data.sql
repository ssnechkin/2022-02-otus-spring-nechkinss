/*

INSERT INTO public.menu(id, position, title, alt, method, link) VALUES
(1, 3, 'Книги', true, 'GET', '/book'),
(2, 2, 'Жанры', true, 'GET', '/genre'),
(3, 1, 'Авторы', true, 'GET', '/author');

INSERT INTO public.acl_class(id, class)	VALUES
(1, 'Menu');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, false, 'ROLE_ADMIN'),
(2, false, 'ROLE_EDITOR'),
(3, false, 'ROLE_VISITOR');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, true),
(2, 1, 2, NULL, 1, true),
(3, 1, 3, NULL, 1, true);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, false, false),
(2, 1, 2, 2, 1, true, false, false),
(3, 1, 3, 3, 1, true, false, false),
(4, 2, 4, 1, 1, true, false, false),
(5, 2, 5, 2, 1, true, false, false),
(6, 2, 6, 3, 1, true, false, false),
(7, 3, 7, 1, 1, true, false, false),
(8, 2, 7, 3, 1, true, false, false);
*/
