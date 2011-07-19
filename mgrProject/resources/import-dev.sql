-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
-------------------------------------------------------------------------------------------------------------------


-- ROLE
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (1, 'Administrator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (2, 'Operator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (3, 'Moderator', true);

-- USERS


