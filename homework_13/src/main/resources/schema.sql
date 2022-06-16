CREATE TABLE IF NOT EXISTS acl_sid (
  id SERIAL NOT NULL PRIMARY KEY,
  principal BOOLEAN NOT NULL,
  sid VARCHAR(100) NOT NULL,
  CONSTRAINT UNIQUE_UK_1 UNIQUE(sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id SERIAL NOT NULL PRIMARY KEY,
  class varchar(255) NOT NULL,
  CONSTRAINT unique_uk_2 UNIQUE(class)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id SERIAL NOT NULL PRIMARY KEY,
  object_id_class bigint NOT NULL,
  object_id_identity bigint NOT NULL,
  parent_object bigint DEFAULT NULL,
  owner_sid bigint DEFAULT NULL,
  entries_inheriting BOOLEAN NOT NULL,
  CONSTRAINT unique_uk_3 UNIQUE(object_id_class,object_id_identity)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id SERIAL NOT NULL PRIMARY KEY,
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