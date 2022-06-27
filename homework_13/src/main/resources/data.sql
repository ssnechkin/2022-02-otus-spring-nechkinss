/*
INSERT INTO public.menu(id, position, title, alt, method, link) VALUES
(1, 3, 'Книги', true, 'GET', '/book'),
(2, 2, 'Жанры', true, 'GET', '/genre'),
(3, 1, 'Авторы', true, 'GET', '/author');

INSERT INTO public.acl_class(id, class)	VALUES
(1, 'ru.otus.homework.domain.entity.Menu');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, true, 'ROLE_ADMIN'),
(2, true, 'ROLE_EDITOR'),
(3, true, 'ROLE_VISITOR');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, false),
(2, 1, 2, NULL, 1, false),
(3, 1, 3, NULL, 1, false);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, true, true),
(2, 1, 2, 2, 1, true, true, true),
(3, 1, 3, 3, 1, true, true, true),
(4, 2, 4, 1, 1, true, true, true),
(5, 2, 5, 2, 1, true, true, true),
(6, 2, 6, 3, 1, true, true, true),
(7, 3, 7, 1, 1, true, true, true),
(8, 2, 7, 3, 1, false, true, true);
*/