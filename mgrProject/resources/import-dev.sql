-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'

insert into users (id, username, password, name) values (0, 'admin', 'admin', 'Administrator');
