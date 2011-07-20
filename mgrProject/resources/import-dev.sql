-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
-------------------------------------------------------------------------------------------------------------------


-- ROLE
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (1, 'Administrator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (2, 'Operator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES (3, 'Moderator', true);

-- USERS
INSERT INTO USERS (id , enabled , firstname , lastname ,  "password" ,  username ,  "version") VALUES(nextval ('public.hibernate_sequence'), TRUE, 'Administrator', 'Administrator', 'admin', 'admin', 0);
INSERT INTO userroles(userid, roleid) VALUES (1, 1);
