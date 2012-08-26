-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
-------------------------------------------------------------------------------------------------------------------

-- Kasuje wszystko z tabeli geometry_columns, postgis wykorzystuje to do sprawdzenia danych 
DELETE FROM geometry_columns;

-- ROLE
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Administrator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Operator', true);
INSERT INTO ROLE(roleid, rolename, conditional) VALUES ( nextval ('public.hibernate_sequence'), 'Moderator', true);

-- USERS
INSERT INTO USERS (id , enabled , firstname , lastname ,  password ,  username ,  version) VALUES(nextval ('public.hibernate_sequence'), TRUE, 'Administrator', 'Administrator', 'admin', 'admin', 0);
INSERT INTO userroles(userid, roleid) VALUES ((SELECT u.id FROM USERS u WHERE u.username='admin'), 1);


-- PRZYSTANKI 

-- AUTOBUSOWE:
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9596028 50.0584513)', 4326), 'Rondo Grzegórzeckie', 'A', 0);
SELECT Populate_Geometry_Columns();-- POSTGIS, wymagane aby zachowac spojnosc w bazie danych (chodzi o odzworodwania SRID)
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469066 50.0756898)', 4326), 'Cmentarz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9455639 50.0569768)', 4326), 'Starowiœlna', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9459175 50.0569637)', 4326), 'Starowiœlna', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9158628 50.0730276)', 4326), 'Biprostal', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9176934 50.0780557)', 4326), 'Mazowiecka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9163887 50.0816579)', 4326), '£obzów PKP', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9476992 50.0761400)', 4326), 'Cmentarz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9485747 50.0781064)', 4326), 'Biskupa Prandoty', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9169042 50.0528696)', 4326), 'Salwator', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9223753 50.0665716)', 4326), 'Czarnowiejska', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9597251 50.0668168)', 4326), 'Rondo Mogilskie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9487737 50.0587173)', 4326), 'Hala Targowa', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9487844 50.0586536)', 4326), 'Hala Targowa', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9677338 50.0491658)', 4326), 'Klimeckiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9293443 50.0526976)', 4326), 'Konopnickiej', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9267252 50.0560778)', 4326), 'Jubilat', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9353019 50.0747759)', 4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259950 50.0688763)', 4326), 'Plac Inwalidów', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9271554 50.0563973)', 4326), 'Jubilat', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9292239 50.0717335)', 4326), 'Grottgera', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9231330 50.0628306)', 4326), 'AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9054824 50.0699547)', 4326), 'Miasteczko Studenckie AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9129460 50.0683881)', 4326), 'Kawiory', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9118612 50.0462661)', 4326), 'Zieliñskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9035332 50.0487627)', 4326), 'Malczewskiego', 'A', 0)
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9046772 50.0564644)', 4326), 'Przegon', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9142421 50.0577042)', 4326), 'Instytut Reumatologii', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9732762 50.0813313)', 4326), 'Rondo M³yñskie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9662214 50.0735677)', 4326), 'Narzymskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9709154 50.0784756)', 4326), 'Pilotów', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9634453 50.0849241)', 4326), 'Lublañska', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9534031 50.0849409)', 4326), 'Opolska Estakada', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9589213 50.0738121)', 4326), 'Cmentarz Rakowicki', 'A', 0);;
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9252526 50.0466826)', 4326), 'Szwedzka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9823369 50.0720096)', 4326), 'Wieczysta', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9446605 50.0710467)', 4326), 'Politechnika', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9434344 50.0672463)', 4326), 'Dworzec G³ówny Zachód (Galeria Krakowska)', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9450070 50.0653699)', 4326), 'Dworzec G³ówny', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9388133 50.0662546)', 4326), 'Basztowa LOT', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9334918 50.0585545)', 4326), 'Filharmonia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9326925 50.0631176)', 4326), 'Teatr Bagatela', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469752 50.0438964)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9274522 50.0840991)', 4326), 'Wybickiego', 'A', 0);


--TRAMWAJOWE PRZYSTNAKI:
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9623698 50.0588172)', 4326), 'Aleja Pokoju', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9761873 50.0595728)', 4326), 'Ofiar D¹bia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9701290 50.0604726)', 4326), 'Fabryczna', 'T', 0);;
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9396020 50.0544414)', 4326), 'Wawel', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9031282 50.0630685)', 4326), 'Cichy K¹cik', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9551055 50.0464802)', 4326), 'Plac Bohaterów Getta', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9613843 50.0418302)', 4326), 'Powstañców Wielkopolskich', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9138452 50.0526568)', 4326), 'Salwator Pêtla', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9019153 50.0771811)', 4326), 'Bronowice', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9410279 50.0586702)', 4326), 'Œwiêtej Gertrudy', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9459501 50.0571036)', 4326), 'Starowiœlna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9414267 50.0516047)', 4326), 'Stradom', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9480358 50.0538743)', 4326), 'Miodowa', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9513231 50.0510636)', 4326), 'Œwiêtego Wawrzyñca', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9194014 50.0719027)', 4326), 'Urzêdnicza', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9106123 50.0742549)', 4326), 'Uniwersytet Pedagogiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9433308 50.0416834)', 4326), 'Smolki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9561903 50.0448955)', 4326), 'Limanowskiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9376526 50.0590720)', 4326), 'Plac Wszystkich Œwiêtych', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259787 50.0346380)', 4326), 'Kobierzyñska', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9241478 50.0414899)', 4326), 'S³omiana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9219418 50.0451159)', 4326), 'Kapelanka', 'T', 0)
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9310490 50.0485980)', 4326), 'Most Grunwaldzki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9387399 50.0506217)', 4326), 'Orzeszkowej', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9172550 50.0526199)', 4326), 'Flisacka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9217424 50.0533630)', 4326), 'Komorowskiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9270441 50.0550182)', 4326), 'Jubilat', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9105429 50.0614228)', 4326), 'Reymana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9314742 50.0604022)', 4326), 'Uniwersytet Jagielloñski', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9488261 50.0586919)', 4326), 'Hala Targowa', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9667636 50.0600095)', 4326), 'Francesco Nullo', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9711448 50.0665741)', 4326), 'Cystersów', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9425710 50.0591765)', 4326), 'Poczta G³ówna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9424868 50.0597907)', 4326), 'Poczta G³ówna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9450511 50.0641670)', 4326), 'Dworzec G³ówny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9510507 50.0650655)', 4326), 'Lubicz', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9527224 50.0683991)', 4326), 'Uniwersytet Ekonomiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9398653 50.0750830)', 4326), 'Dworzec Towarowy', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9377104 50.0807491)', 4326), 'Pr¹dnicka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9336249 50.0847079)', 4326), 'Bratys³awska', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.8977928 50.0788760)', 4326), 'Wesele', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9056435 50.0759979)', 4326), 'G³owackiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469900 50.0496600)', 4326), 'Muzeum In¿ynierii Miejskiej', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9376160 50.0695030)', 4326), 'Pêdzichów', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9212159 50.0598187)', 4326), 'Oleandry', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9644057 50.0499573)', 4326), 'Zab³ocie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9579869 50.0576053)', 4326), 'Rondo Grzegórzeckie', 'T', 0);






-- LINIA  i TABLICZKI

INSERT INTO linie(id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 123, 'A', 0);

INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 13, 0, 1); -- ID 97
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 14,  97, 1, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 98, version=version+1 WHERE id = 97;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 16, 98, 2, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 99, version=version+1 WHERE id = 98;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 6, 99, 3, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 100, version=version+1 WHERE id = 99;
INSERT INTO przystanek_tabliczki(id, version, linia_id, przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 5, 100, 4, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 101, version=version+1 WHERE id = 100;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 7,101, 5, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 102, version=version+1 WHERE id = 101;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 10,102, 6, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 103, version=version+1 WHERE id = 102;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 96, 12,103, 7, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 104, version=version+1 WHERE id = 103;

INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 3, 'T', 0); --id 105
INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 501, 'A', 0); 
INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 899, 'A', 0); -- ID 107

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 105, NULL, 95, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2,105, 108, 73, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 109, version=version+1 WHERE id = 108;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 105, 109, 74, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 110, version=version+1 WHERE id = 109;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 105, 110, 75, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 111, version=version+1 WHERE id = 110;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 106, NULL, 22, 0); -- IU 112
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 106, 112, 25, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 113, version=version+1 WHERE id = 112
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 106, 113, 23, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 114, version=version+1 WHERE id = 113;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 106, 114, 26, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 115, version=version+1 WHERE id = 114;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 106, 115, 21, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 116, version=version+1 WHERE id = 115;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 2, 2, 107, NULL, 43, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 2, 2, 107, 117, 45, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 118, version=version+1 WHERE id =117;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 107, 118, 23, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 119, version=version+1 WHERE id = 118;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 107, 119, 15, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 120, version=version+1 WHERE id = 119;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 107, 120, 28, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 121, version=version+1 WHERE id = 120;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 107, 121,23, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 122, version=version+1 WHERE id = 121;



-- ODJAZDY
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:36:00', 'DZIEN_POWSZEDNI', 0, 97);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:37:00', 'DZIEN_POWSZEDNI', 0, 98);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:38:00', 'DZIEN_POWSZEDNI', 0, 99);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:39:00', 'DZIEN_POWSZEDNI', 0, 100);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:40:00', 'DZIEN_POWSZEDNI', 0, 101);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:41:00', 'DZIEN_POWSZEDNI', 0, 102);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:42:00', 'DZIEN_POWSZEDNI', 0, 103);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:43:00', 'DZIEN_POWSZEDNI', 0, 104);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:22:00', 'SWIETA', 1, 97);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:23:00', 'SWIETA', 1, 98);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:24:00', 'SWIETA', 1, 99);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:25:00', 'SWIETA', 1, 100);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:26:00', 'SWIETA', 1, 101);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:27:00', 'SWIETA', 1, 102);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:28:00', 'SWIETA', 1, 103);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:29:00', 'SWIETA', 1, 104);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:31:00', 'DZIEN_POWSZEDNI', 0, 108);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:30:00', 'SWIETA', 0, 108);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:32:00', 'DZIEN_POWSZEDNI', 0, 109);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:31:00', 'SWIETA', 0, 109);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:33:00', 'DZIEN_POWSZEDNI', 0, 110);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:32:00', 'SWIETA', 0, 110);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:34:00', 'DZIEN_POWSZEDNI', 0, 111);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:33:00', 'SWIETA', 0, 111);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'DZIEN_POWSZEDNI', 0, 112);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'SWIETA', 0, 112);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:04:00', 'DZIEN_POWSZEDNI', 0, 113);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:04:00', 'SWIETA', 0, 113);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'DZIEN_POWSZEDNI', 0, 114);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'SWIETA', 0, 114);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:06:00', 'DZIEN_POWSZEDNI', 0, 115);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:06:00', 'SWIETA', 0, 115);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'DZIEN_POWSZEDNI', 0, 116);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'SWIETA', 0, 116);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'DZIEN_POWSZEDNI', 0, 117);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'SWIETA', 0, 117);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'DZIEN_POWSZEDNI', 0, 118);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'SWIETA', 0, 119);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'DZIEN_POWSZEDNI', 0, 119);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'SWIETA', 0, 119);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:08:00', 'DZIEN_POWSZEDNI', 0, 120);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:08:00', 'SWIETA', 0, 120);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:09:00', 'DZIEN_POWSZEDNI', 0, 121);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:09:00', 'SWIETA', 0, 121);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:10:00', 'DZIEN_POWSZEDNI', 0, 122);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:10:00', 'SWIETA', 0, 122);


-- KONFIGURACJA
INSERT INTO konfiguracja(id, liczbawatkow, name, nieskonczonosc, odlegloscprzystankow, odlegloscdostartstop, predkoscpasazera, version) VALUES (nextval('public.hibernate_sequence'), 0, 'default', 9999, 200, 500, 6.0, 0)


-- USERS
INSERT INTO USERS (id , enabled , firstname , lastname ,  password ,  username ,  version) VALUES(nextval ('public.hibernate_sequence'), TRUE, 'Operator', 'Operator', 'oper', 'oper', 0);
INSERT INTO userroles(userid, roleid) VALUES ((SELECT u.id FROM USERS u WHERE u.username='oper'), 2);
INSERT INTO USERS (id , enabled , firstname , lastname ,  password ,  username ,  version) VALUES(nextval ('public.hibernate_sequence'), TRUE, 'Moderator', 'Moderator', 'moder', 'moder', 0);
INSERT INTO userroles(userid, roleid) VALUES ((SELECT u.id FROM USERS u WHERE u.username='moder'), 3);




INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 666, 'A', 1); --300 172
INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 777, 'A', 0); --322 173
INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 321, 'T', 0); --331 174
INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 445, 'A', 0); --388 175
INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 356, 'T', 0); --391 176


INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 172,  NULL, 42, 0); -- 177
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 3, 172,  177, 43, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 178, version=version+1 WHERE id = 177;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 3, 172,  178, 46, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 179, version=version+1 WHERE id = 178;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 172,  179, 45, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 180, version=version+1 WHERE id = 179;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 172,  180, 24, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 181, version=version+1 WHERE id = 180;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 172,  181, 14, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 182, version=version+1 WHERE id = 181;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 15, 3, 173,  NULL, 23, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 173,  183, 46, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 184, version=version+1 WHERE id = 183;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 173,  184, 45, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 185, version=version+1 WHERE id = 184;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 173,  185, 8, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 186, version=version+1 WHERE id = 185;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  NULL, 49, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  187, 95, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 188, version=version+1 WHERE id = 187;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  188, 78, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 189, version=version+1 WHERE id = 188;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  189, 59, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 190, version=version+1 WHERE id = 189;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  190, 81, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 191, version=version+1 WHERE id = 190;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 174,  191, 58, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 192, version=version+1 WHERE id = 191;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 174,  192, 52, 6);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 193, version=version+1 WHERE id = 192;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 175,  NULL, 19, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 0, 175,  194, 17, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 195, version=version+1 WHERE id = 194;


INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 176,  NULL, 83, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 176, 196, 82, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 197, version=version+1 WHERE id = 196;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 176,  197, 81, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 198, version=version+1 WHERE id = 197;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 176,  198, 59, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 199, version=version+1 WHERE id = 198;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 176,  199, 61, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 200, version=version+1 WHERE id = 199;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 176, 200, 62, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 201, version=version+1 WHERE id = 200;

INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 177);--202
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:40:00', 'DZIEN_POWSZEDNI', 0, 177);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:05:00', 'DZIEN_POWSZEDNI', 0, 178);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:45:00', 'DZIEN_POWSZEDNI', 0, 178);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:10:00', 'DZIEN_POWSZEDNI', 0, 179);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:50:00', 'DZIEN_POWSZEDNI', 0, 179);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:15:00', 'DZIEN_POWSZEDNI', 0, 180);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:55:00', 'DZIEN_POWSZEDNI', 0, 180);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:20:00', 'DZIEN_POWSZEDNI', 0, 181);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:00:00', 'DZIEN_POWSZEDNI', 0, 181);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:25:00', 'DZIEN_POWSZEDNI', 0, 182);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:05:00', 'DZIEN_POWSZEDNI', 0, 182);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:30:00', 'DZIEN_POWSZEDNI', 0, 183);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:10:00', 'DZIEN_POWSZEDNI', 0, 183);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:45:00', 'DZIEN_POWSZEDNI', 0, 184);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:25:00', 'DZIEN_POWSZEDNI', 0, 184);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:16:00', 'DZIEN_POWSZEDNI', 0, 185);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:26:00', 'DZIEN_POWSZEDNI', 0, 185);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:17:00', 'DZIEN_POWSZEDNI', 0, 186);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:27:00', 'DZIEN_POWSZEDNI', 0, 186);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:00:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '22:47:00', 'DZIEN_POWSZEDNI', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:05:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '22:52:00', 'DZIEN_POWSZEDNI', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:10:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '22:57:00', 'DZIEN_POWSZEDNI', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:15:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:02:00', 'DZIEN_POWSZEDNI', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:20:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:07:00', 'DZIEN_POWSZEDNI', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:25:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:12:00', 'DZIEN_POWSZEDNI', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:30:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:17:00', 'DZIEN_POWSZEDNI', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 194);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'DZIEN_POWSZEDNI', 0, 194);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 194);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:10:00', 'DZIEN_POWSZEDNI', 0, 194);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:05:00', 'DZIEN_POWSZEDNI', 0, 195);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:35:00', 'DZIEN_POWSZEDNI', 0, 195);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:05:00', 'DZIEN_POWSZEDNI', 0, 195);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:15:00', 'DZIEN_POWSZEDNI', 0, 195);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:10:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:40:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:10:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:20:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:15:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:45:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:15:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:25:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:20:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:50:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:20:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:30:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:25:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:55:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:25:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:35:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:30:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:40:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:35:00', 'DZIEN_POWSZEDNI', 0, 201);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:05:00', 'DZIEN_POWSZEDNI', 0, 201);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:35:00', 'DZIEN_POWSZEDNI', 0, 201);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '23:45:00', 'DZIEN_POWSZEDNI', 0, 201); --302

INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 369, 'A', 0);

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 303, NULL, 22, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 303, 304, 44, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 305, version=version+1 WHERE id = 304;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 303, 305, 46, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 306, version=version+1 WHERE id = 305;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 303, 306, 45, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 307, version=version+1 WHERE id = 306;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 303, 307, 24, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 308, version=version+1 WHERE id = 307;

INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 357, 'T', 0);

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 309, NULL, 84, 0); -- 310
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 310, 83, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 311, version=version+1 WHERE id = 310;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 311, 82, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 312, version=version+1 WHERE id = 311;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 312, 58, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 313, version=version+1 WHERE id = 312;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 313, 52, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 314, version=version+1 WHERE id = 313;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 314, 60, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 315, version=version+1 WHERE id = 314;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 315, 72, 6);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 316, version=version+1 WHERE id = 315;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 316, 71, 7);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 317, version=version+1 WHERE id = 316;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 317, 70, 8);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 318, version=version+1 WHERE id = 317;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 309, 318, 69, 9);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 319, version=version+1 WHERE id = 318;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 309, 319, 68, 10);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 320, version=version+1 WHERE id = 319;

INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:00:00', 'DZIEN_POWSZEDNI', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'SWIETA', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:30:00', 'SWIETA', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:28:00', 'SWIETA', 0, 310);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:01:00', 'DZIEN_POWSZEDNI', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:00', 'SWIETA', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:31:00', 'SWIETA', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:29:53', 'SWIETA', 0, 311);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:06:00', 'DZIEN_POWSZEDNI', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:36:00', 'SWIETA', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:36:00', 'SWIETA', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:34:53', 'SWIETA', 0, 312);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:11:00', 'DZIEN_POWSZEDNI', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:41:00', 'SWIETA', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:41:00', 'SWIETA', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:39:53', 'SWIETA', 0, 313);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:16:00', 'DZIEN_POWSZEDNI', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:46:00', 'SWIETA', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:46:00', 'SWIETA', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:44:53', 'SWIETA', 0, 314);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:21:00', 'DZIEN_POWSZEDNI', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:51:00', 'SWIETA', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:51:00', 'SWIETA', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:49:53', 'SWIETA', 0, 315);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:26:00', 'DZIEN_POWSZEDNI', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:56:00', 'SWIETA', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:56:00', 'SWIETA', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:54:53', 'SWIETA', 0, 316);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:31:00', 'DZIEN_POWSZEDNI', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:01:00', 'SWIETA', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:01:00', 'SWIETA', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:59:53', 'SWIETA', 0, 317);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:36:00', 'DZIEN_POWSZEDNI', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:06:00', 'SWIETA', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:06:00', 'SWIETA', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:04:53', 'SWIETA', 0, 318);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:41:00', 'DZIEN_POWSZEDNI', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:11:00', 'SWIETA', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:11:00', 'SWIETA', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:09:53', 'SWIETA', 0, 319);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '14:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:46:00', 'DZIEN_POWSZEDNI', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:16:00', 'SWIETA', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:16:00', 'SWIETA', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:14:53', 'SWIETA', 0, 320);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:00:00', 'DZIEN_POWSZEDNI', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:00:00', 'DZIEN_POWSZEDNI', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:00:00', 'DZIEN_POWSZEDNI', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'SWIETA', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:30:00', 'SWIETA', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:11', 'SWIETA', 0, 304);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:01:00', 'DZIEN_POWSZEDNI', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:01:00', 'DZIEN_POWSZEDNI', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:01:00', 'DZIEN_POWSZEDNI', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:01:00', 'DZIEN_POWSZEDNI', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:01:00', 'DZIEN_POWSZEDNI', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:00', 'SWIETA', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:31:00', 'SWIETA', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:32:11', 'SWIETA', 0, 305);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:06:00', 'DZIEN_POWSZEDNI', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:06:00', 'DZIEN_POWSZEDNI', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:06:00', 'DZIEN_POWSZEDNI', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:06:00', 'DZIEN_POWSZEDNI', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:06:00', 'DZIEN_POWSZEDNI', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:36:00', 'SWIETA', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:36:00', 'SWIETA', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:37:11', 'SWIETA', 0, 306);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:11:00', 'DZIEN_POWSZEDNI', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:11:00', 'DZIEN_POWSZEDNI', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:11:00', 'DZIEN_POWSZEDNI', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:11:00', 'DZIEN_POWSZEDNI', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:11:00', 'DZIEN_POWSZEDNI', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:41:00', 'SWIETA', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:41:00', 'SWIETA', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:42:11', 'SWIETA', 0, 307);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:16:00', 'DZIEN_POWSZEDNI', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:16:00', 'DZIEN_POWSZEDNI', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:16:00', 'DZIEN_POWSZEDNI', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:16:00', 'DZIEN_POWSZEDNI', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:16:00', 'DZIEN_POWSZEDNI', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:46:00', 'SWIETA', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:46:00', 'SWIETA', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:47:11', 'SWIETA', 0, 308);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'SWIETA', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:30:00', 'SWIETA', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:37:24', 'SWIETA', 0, 187);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:35:00', 'SWIETA', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:35:00', 'SWIETA', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:42:24', 'SWIETA', 0, 188);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:40:00', 'SWIETA', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:40:00', 'SWIETA', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:47:24', 'SWIETA', 0, 189);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:45:00', 'SWIETA', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:45:00', 'SWIETA', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:52:24', 'SWIETA', 0, 190);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:50:00', 'SWIETA', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:50:00', 'SWIETA', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:57:24', 'SWIETA', 0, 191);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:55:00', 'SWIETA', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:55:00', 'SWIETA', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:02:24', 'SWIETA', 0, 192);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'SWIETA', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:00:00', 'SWIETA', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:07:24', 'SWIETA', 0, 193);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:10:00', 'DZIEN_POWSZEDNI', 0, 183);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:00:00', 'DZIEN_POWSZEDNI', 0, 183);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:38:36', 'DZIEN_POWSZEDNI', 0, 183);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:25:00', 'DZIEN_POWSZEDNI', 0, 184);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:15:00', 'DZIEN_POWSZEDNI', 0, 184);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:53:36', 'DZIEN_POWSZEDNI', 0, 184);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:26:00', 'DZIEN_POWSZEDNI', 0, 185);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:16:00', 'DZIEN_POWSZEDNI', 0, 185);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:54:36', 'DZIEN_POWSZEDNI', 0, 185);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:27:00', 'DZIEN_POWSZEDNI', 0, 186);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:17:00', 'DZIEN_POWSZEDNI', 0, 186);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:55:36', 'DZIEN_POWSZEDNI', 0, 186);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:00:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:00:00', 'DZIEN_POWSZEDNI', 0, 196);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:05:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:05:00', 'DZIEN_POWSZEDNI', 0, 197);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:10:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:10:00', 'DZIEN_POWSZEDNI', 0, 198);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:15:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:15:00', 'DZIEN_POWSZEDNI', 0, 199);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:20:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:20:00', 'DZIEN_POWSZEDNI', 0, 200);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:25:00', 'DZIEN_POWSZEDNI', 0, 201);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:25:00', 'DZIEN_POWSZEDNI', 0, 201); -- 504

INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 387, 'T', 0);

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 505, NULL, 55, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 506, 66, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 507, version=version+1 WHERE id = 506;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 507, 54, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 508, version=version+1 WHERE id = 507;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 508, 62, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 509, version=version+1 WHERE id = 508;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 509, 61, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 510, version=version+1 WHERE id = 509;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 510, 59, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 511, version=version+1 WHERE id = 510;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 511, 81, 6);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 512, version=version+1 WHERE id = 511;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 512, 82, 7);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 513, version=version+1 WHERE id = 512;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 505, 513, 83, 8);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 514, version=version+1 WHERE id = 513;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 505, 514, 84, 9); --515
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 515, version=version+1 WHERE id = 514;

INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:00:00', 'DZIEN_POWSZEDNI', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:30:00', 'SWIETA', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:30:00', 'SWIETA', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:30:00', 'SWIETA', 0, 506);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:01:00', 'DZIEN_POWSZEDNI', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:00', 'SWIETA', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:31:00', 'SWIETA', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:31:00', 'SWIETA', 0, 507);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:06:00', 'DZIEN_POWSZEDNI', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:36:00', 'SWIETA', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:36:00', 'SWIETA', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:36:00', 'SWIETA', 0, 508);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:11:00', 'DZIEN_POWSZEDNI', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:41:00', 'SWIETA', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:41:00', 'SWIETA', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:41:00', 'SWIETA', 0, 509);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:16:00', 'DZIEN_POWSZEDNI', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:46:00', 'SWIETA', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:46:00', 'SWIETA', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:46:00', 'SWIETA', 0, 510);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:21:00', 'DZIEN_POWSZEDNI', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:51:00', 'SWIETA', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:51:00', 'SWIETA', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:51:00', 'SWIETA', 0, 511);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:26:00', 'DZIEN_POWSZEDNI', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:56:00', 'SWIETA', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:56:00', 'SWIETA', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:56:00', 'SWIETA', 0, 512);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:31:00', 'DZIEN_POWSZEDNI', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:01:00', 'SWIETA', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:01:00', 'SWIETA', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:01:00', 'SWIETA', 0, 513);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:36:00', 'DZIEN_POWSZEDNI', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:06:00', 'SWIETA', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:06:00', 'SWIETA', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:06:00', 'SWIETA', 0, 514);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '20:41:00', 'DZIEN_POWSZEDNI', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '11:11:00', 'SWIETA', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '16:11:00', 'SWIETA', 0, 515);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '19:11:00', 'SWIETA', 0, 515);

INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 105, 'A', 0); -- id 606

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 606, NULL, 9, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 606, 607, 23, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 608, version=version+1 WHERE id = 607;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 606, 608, 46, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 609, version=version+1 WHERE id = 608;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 606, 609, 45, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 610, version=version+1 WHERE id = 609;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 606, 610, 20, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 611, version=version+1 WHERE id = 610;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 606, 611, 39, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 612, version=version+1 WHERE id = 611;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 606, 612, 29, 6);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 613, version=version+1 WHERE id = 612;

INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:00:00', 'DZIEN_POWSZEDNI', 0, 607);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 607);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:00:00', 'DZIEN_POWSZEDNI', 0, 607);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:00:00', 'DZIEN_POWSZEDNI', 0, 607);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:00:00', 'DZIEN_POWSZEDNI', 0, 607);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:01:00', 'DZIEN_POWSZEDNI', 0, 608);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:01:00', 'DZIEN_POWSZEDNI', 0, 608);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:01:00', 'DZIEN_POWSZEDNI', 0, 608);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:01:00', 'DZIEN_POWSZEDNI', 0, 608);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:01:00', 'DZIEN_POWSZEDNI', 0, 608);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:06:00', 'DZIEN_POWSZEDNI', 0, 609);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:06:00', 'DZIEN_POWSZEDNI', 0, 609);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:06:00', 'DZIEN_POWSZEDNI', 0, 609);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:06:00', 'DZIEN_POWSZEDNI', 0, 609);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:06:00', 'DZIEN_POWSZEDNI', 0, 609);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:11:00', 'DZIEN_POWSZEDNI', 0, 610);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:11:00', 'DZIEN_POWSZEDNI', 0, 610);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:11:00', 'DZIEN_POWSZEDNI', 0, 610);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:11:00', 'DZIEN_POWSZEDNI', 0, 610);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:11:00', 'DZIEN_POWSZEDNI', 0, 610);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:16:00', 'DZIEN_POWSZEDNI', 0, 611);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:16:00', 'DZIEN_POWSZEDNI', 0, 611);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:16:00', 'DZIEN_POWSZEDNI', 0, 611);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:16:00', 'DZIEN_POWSZEDNI', 0, 611);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:16:00', 'DZIEN_POWSZEDNI', 0, 611);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:21:00', 'DZIEN_POWSZEDNI', 0, 612);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:21:00', 'DZIEN_POWSZEDNI', 0, 612);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:21:00', 'DZIEN_POWSZEDNI', 0, 612);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:21:00', 'DZIEN_POWSZEDNI', 0, 612);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:21:00', 'DZIEN_POWSZEDNI', 0, 612);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:26:00', 'DZIEN_POWSZEDNI', 0, 613);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:26:00', 'DZIEN_POWSZEDNI', 0, 613);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:26:00', 'DZIEN_POWSZEDNI', 0, 613);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:26:00', 'DZIEN_POWSZEDNI', 0, 613);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:26:00', 'DZIEN_POWSZEDNI', 0, 613); --648

INSERT INTO linie (id, numer, typ, version) VALUES (nextval ('public.hibernate_sequence'), 106, 'A', 0);

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 2, 649, NULL, 29, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 649, 650, 39, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 651, version=version+1 WHERE id = 650;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 649, 651, 20, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 652, version=version+1 WHERE id = 651;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 649, 652, 45, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 653, version=version+1 WHERE id = 652;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 649, 653, 46, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 654, version=version+1 WHERE id = 653;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 5, 2, 649, 654, 23, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 655, version=version+1 WHERE id = 654;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval ('public.hibernate_sequence'), 1, 1, 649, 655, 9, 6);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 656, version=version+1 WHERE id = 655;

INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:00:00', 'DZIEN_POWSZEDNI', 0, 650);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:00:00', 'DZIEN_POWSZEDNI', 0, 650);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:00:00', 'DZIEN_POWSZEDNI', 0, 650);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:00:00', 'DZIEN_POWSZEDNI', 0, 650);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:00:00', 'DZIEN_POWSZEDNI', 0, 650);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:01:00', 'DZIEN_POWSZEDNI', 0, 651);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:01:00', 'DZIEN_POWSZEDNI', 0, 651);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:01:00', 'DZIEN_POWSZEDNI', 0, 651);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:01:00', 'DZIEN_POWSZEDNI', 0, 651);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:01:00', 'DZIEN_POWSZEDNI', 0, 651);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:06:00', 'DZIEN_POWSZEDNI', 0, 652);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:06:00', 'DZIEN_POWSZEDNI', 0, 652);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:06:00', 'DZIEN_POWSZEDNI', 0, 652);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:06:00', 'DZIEN_POWSZEDNI', 0, 652);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:06:00', 'DZIEN_POWSZEDNI', 0, 652);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:11:00', 'DZIEN_POWSZEDNI', 0, 653);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:11:00', 'DZIEN_POWSZEDNI', 0, 653);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:11:00', 'DZIEN_POWSZEDNI', 0, 653);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:11:00', 'DZIEN_POWSZEDNI', 0, 653);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:11:00', 'DZIEN_POWSZEDNI', 0, 653);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:16:00', 'DZIEN_POWSZEDNI', 0, 654);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:16:00', 'DZIEN_POWSZEDNI', 0, 654);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:16:00', 'DZIEN_POWSZEDNI', 0, 654);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:16:00', 'DZIEN_POWSZEDNI', 0, 654);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:16:00', 'DZIEN_POWSZEDNI', 0, 654);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:21:00', 'DZIEN_POWSZEDNI', 0, 655);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:21:00', 'DZIEN_POWSZEDNI', 0, 655);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:21:00', 'DZIEN_POWSZEDNI', 0, 655);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:21:00', 'DZIEN_POWSZEDNI', 0, 655);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:21:00', 'DZIEN_POWSZEDNI', 0, 655);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '09:26:00', 'DZIEN_POWSZEDNI', 0, 656);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '10:26:00', 'DZIEN_POWSZEDNI', 0, 656);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '12:26:00', 'DZIEN_POWSZEDNI', 0, 656);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '15:26:00', 'DZIEN_POWSZEDNI', 0, 656);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval ('public.hibernate_sequence'), '18:26:00', 'DZIEN_POWSZEDNI', 0, 656); --691


