-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
-------------------------------------------------------------------------------------------------------------------

-- Kasuje wszystko z tabeli geometry_columns, postgis wykorzystuje to do sprawdzenia danych 
DELETE FROM geometry_columns;

-- ROLE
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Administrator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Operator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Moderator', true);

-- USERS
INSERT INTO USERS (id , enabled , firstname , lastname ,  "password" ,  username ,  "version") VALUES(nextval ('public.hibernate_sequence'), TRUE, 'Administrator', 'Administrator', 'admin', 'admin', 0);
INSERT INTO userroles(userid, roleid) VALUES ((SELECT u.id FROM USERS u WHERE u.username='admin'), 1);


-- PRZYSTANKI 


INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9330075826 50.0636098319)',4326),  'Teatr Bagatela', 'A', 0);
SELECT Populate_Geometry_Columns();-- POSTGIS, wymagane aby zachowac spojnosc w bazie danych (chodzi o odzworodwania SRID)
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9327179041 50.0631724906)',4326), 'Teatr Bagatela', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9293007698 50.0667606381)',4326), 'Batorego ', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9269028749 50.0688438443)',4326), 'Plac Inwalidow', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259104576 50.0695428172)',4326), 'Plac Inwalidow', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9260070171 50.0688748335)',4326), 'Plac Inwalidow', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.926967248 50.0693224524)',4326), 'Plac Inwalidow', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9371864643 50.0736228251)',4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9351479855 50.0738087413)',4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9363066997 50.0734954377)',4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9376156177 50.0694980557)',4326), 'Pedzichow', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9389459934 50.0666022418)',4326), 'Basztowa LOT', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9386885013 50.0663061083)',4326), 'Basztowa LOT', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9230458584 50.0628487865)',4326), 'AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9243547764 50.064253784)',4326), 'AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9251433458 50.0598182597)',4326), 'Cracovia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9251755323 50.0586094393)',4326), 'Cracovia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9242260304 50.059325781)',4326), 'Cracovia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9257146564 50.0595737429)',4326), 'Cracovia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9271335449 50.0565068459)',4326), 'Jubilat', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9266561117 50.0561589878)',4326), 'Jubilat', 'A', 0);


-- LINIA  i TABLICZKI

INSERT INTO linie(id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 123, 'A', 0);

INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 18, 0, 1);
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 21,  27, 1, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 28, version=version+1 WHERE id = 27;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 25, 28, 2, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 29, version=version+1 WHERE id = 28;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 6, 29, 3, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 30, version=version+1 WHERE id = 29;
INSERT INTO przystanek_tabliczki(id, version, linia_id, przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 5, 30, 4, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 31, version=version+1 WHERE id = 30;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 11,31, 5, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 32, version=version+1 WHERE id = 31;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 14,32, 6, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 33, version=version+1 WHERE id = 32;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 26, 16,33, 7, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 34, version=version+1 WHERE id = 33;


