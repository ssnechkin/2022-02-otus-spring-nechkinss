CREATE TABLE IF NOT EXISTS menu (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  position int NOT NULL,
  title VARCHAR(100) NOT NULL,
  alt BOOLEAN NOT NULL,
  method VARCHAR(255) NOT NULL,
  link VARCHAR(255) NOT NULL,
  CONSTRAINT UNIQUE_UK_0 UNIQUE(title, link, method)
);

CREATE TABLE IF NOT EXISTS acl_sid (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  principal BOOLEAN NOT NULL,
  sid varchar(255) NOT NULL,
  CONSTRAINT UNIQUE_UK_1 UNIQUE(sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  class varchar(255) NOT NULL,
  CONSTRAINT unique_uk_2 UNIQUE(class)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  object_id_class bigint NOT NULL,
  object_id_identity varchar NOT NULL,
  parent_object bigint,
  owner_sid bigint,
  entries_inheriting BOOLEAN NOT NULL,
  CONSTRAINT unique_uk_3 UNIQUE(object_id_class,object_id_identity)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  acl_object_identity bigint NOT NULL,
  ace_order int NOT NULL,
  sid bigint NOT NULL,
  mask int NOT NULL,
  granting BOOLEAN NOT NULL,
  audit_success BOOLEAN NOT NULL,
  audit_failure BOOLEAN NOT NULL,
  CONSTRAINT unique_uk_4 UNIQUE(acl_object_identity,ace_order)
);

ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);