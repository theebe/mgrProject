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

-- AUTOBUSOWE:
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9596028 50.0584513)', 4326), 'Rondo Grzeg?zeckie', 'A', 0);
SELECT Populate_Geometry_Columns();-- POSTGIS, wymagane aby zachowac spojnosc w bazie danych (chodzi o odzworodwania SRID)
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469066 50.0756898)', 4326), 'Cmentarz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9430821 50.0484324)', 4326), 'Plac Wolnica', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9435445 50.0478622)', 4326), 'Plac Wolnica', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9409978 50.0517469)', 4326), 'Stradom', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9421949 50.0519242)', 4326), 'Stradom', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9455639 50.0569768)', 4326), 'Starowi?na', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9459175 50.0569637)', 4326), 'Starowi?na', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9158628 50.0730276)', 4326), 'Biprostal', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9176934 50.0780557)', 4326), 'Mazowiecka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9163887 50.0816579)', 4326), '?bz? PKP', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9476992 50.0761400)', 4326), 'Cmentarz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9466864 50.0761290)', 4326), 'Cmentarz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9485747 50.0781064)', 4326), 'Biskupa Prandoty', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9500497 50.0792343)', 4326), 'Biskupa Prandoty', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9040599 50.0490388)', 4326), 'Malczewskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9169042 50.0528696)', 4326), 'Salwator', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9223753 50.0665716)', 4326), 'Czarnowiejska', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9232233 50.0662026)', 4326), 'Czarnowiejska', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9173745 50.0675628)', 4326), 'Kawiory', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9597251 50.0668168)', 4326), 'Rondo Mogilskie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9578272 50.0656082)', 4326), 'Rondo Mogilskie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9466977 50.0436250)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9466940 50.0440566)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9252186 50.0467167)', 4326), 'Szwedzka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9311253 50.0485715)', 4326), 'Most Grunwaldzki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9309787 50.0486422)', 4326), 'Most Grunwaldzki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9323276 50.0483103)', 4326), 'Most Grunwaldzki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9331191 50.0485873)', 4326), 'Most Grunwaldzki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9322178 50.0497888)', 4326), 'Most Grunwaldzki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9036434 50.0629844)', 4326), 'Cichy K?ik', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9487737 50.0587173)', 4326), 'Hala Targowa', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9487844 50.0586536)', 4326), 'Hala Targowa', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9062535 50.0696584)', 4326), 'Miasteczko Studenckie AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9683503 50.0483221)', 4326), 'Klimeckiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9677338 50.0491658)', 4326), 'Klimeckiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9293443 50.0526976)', 4326), 'Konopnickiej', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9295897 50.0527764)', 4326), 'Konopnickiej', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9267252 50.0560778)', 4326), 'Jubilat', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9351360 50.0738045)', 4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9353019 50.0747759)', 4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9243347 50.0642689)', 4326), 'AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259950 50.0688763)', 4326), 'Plac Inwalid?', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9271554 50.0563973)', 4326), 'Jubilat', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9371727 50.0736245)', 4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9292239 50.0717335)', 4326), 'Grottgera', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9231330 50.0628306)', 4326), 'AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9363818 50.0742044)', 4326), 'Nowy Kleparz', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9300765 50.0716384)', 4326), 'Grottgera', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9269714 50.0693254)', 4326), 'Plac Inwalid?', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9036769 50.0697879)', 4326), 'Miasteczko Studenckie AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9139937 50.0687763)', 4326), 'Kawiory', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9136669 50.0684785)', 4326), 'Kawiory', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9054824 50.0699547)', 4326), 'Miasteczko Studenckie AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9042196 50.0703386)', 4326), 'Miasteczko Studenckie AGH', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9129460 50.0683881)', 4326), 'Kawiory', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9118612 50.0462661)', 4326), 'Zieli?kiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9031729 50.0497776)', 4326), 'Malczewskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9111317 50.0472137)', 4326), 'Zieli?kiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9035332 50.0487627)', 4326), 'Malczewskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9137729 50.0577746)', 4326), 'Instytut Reumatologii', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9053883 50.0564528)', 4326), 'Przegon', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9046772 50.0564644)', 4326), 'Przegon', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9142421 50.0577042)', 4326), 'Instytut Reumatologii', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9026989 50.0628470)', 4326), 'Cichy K?ik', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9732762 50.0813313)', 4326), 'Rondo M??kie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9711913 50.0831566)', 4326), 'Olsza II', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9662214 50.0735677)', 4326), 'Narzymskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9728267 50.0803303)', 4326), 'Rondo M??kie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9703936 50.0834005)', 4326), 'Olsza II', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9655424 50.0732795)', 4326), 'Narzymskiego', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9709154 50.0784756)', 4326), 'Pilot?', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9715629 50.0788022)', 4326), 'Pilot?', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9687687 50.0848874)', 4326), 'Olsza II', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9634453 50.0849241)', 4326), 'Lubla?ka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9534031 50.0849409)', 4326), 'Opolska Estakada', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9589213 50.0738121)', 4326), 'Cmentarz Rakowicki', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9602058 50.0576630)', 4326), 'Rondo Grzeg?zeckie', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9252526 50.0466826)', 4326), 'Szwedzka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9221196 50.0455866)', 4326), 'Kapelanka', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9823369 50.0720096)', 4326), 'Wieczysta', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9446605 50.0710467)', 4326), 'Politechnika', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9434344 50.0672463)', 4326), 'Dworzec G?wny Zach? (Galeria Krakowska)', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9443630 50.0672519)', 4326), 'Dworzec G?wny Zach? (Galeria Krakowska)', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9442151 50.0647927)', 4326), 'Dworzec G?wny', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9452691 50.0678263)', 4326), 'Dworzec G?wny Zach? (Galeria)', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9450070 50.0653699)', 4326), 'Dworzec G?wny', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9406846 50.0754888)', 4326), 'Dworzec Towarowy', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9150215 50.0731223)', 4326), 'Biprostal', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9330626 50.0636341)', 4326), 'Teatr Bagatela', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9388133 50.0662546)', 4326), 'Basztowa LOT', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9401436 50.0663304)', 4326), 'Basztowa LOT', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9419890 50.0526822)', 4326), 'Stradom', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9334918 50.0585545)', 4326), 'Filharmonia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9334810 50.0580551)', 4326), 'Filharmonia', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9326925 50.0631176)', 4326), 'Teatr Bagatela', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469752 50.0438964)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9484102 50.0445872)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9467902 50.0436122)', 4326), 'Korona', 'A', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9274522 50.0840991)', 4326), 'Wybickiego', 'A', 0);


--TRAMWAJOWE PRZYSTNAKI:
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9394981 50.0663142)', 4326), 'Basztowa LOT', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9706039 50.0665438)', 4326), 'Cysters?', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9593347 50.0649252)', 4326), 'Rondo Mogilskie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9623698 50.0588300)', 4326), 'Aleja Pokoju', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9761873 50.0595728)', 4326), 'Ofiar D?ia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9701290 50.0604726)', 4326), 'Fabryczna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9674032 50.0601676)', 4326), 'Francesco Nullo', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9341206 50.0837180)', 4326), 'Bratys?wska', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9363174 50.0734957)', 4326), 'Nowy Kleparz', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9396020 50.0544414)', 4326), 'Wawel', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9451123 50.0709653)', 4326), 'Politechnika', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9281326 50.0556208)', 4326), 'Jubilat', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9031282 50.0630685)', 4326), 'Cichy K?ik', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9551055 50.0464802)', 4326), 'Plac Bohater? Getta', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9606345 50.0424598)', 4326), 'Powsta?? Wielkopolskich', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9613843 50.0418302)', 4326), 'Powsta?? Wielkopolskich', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9138452 50.0526568)', 4326), 'Salwator P?la', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9019153 50.0771811)', 4326), 'Bronowice', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9433805 50.0480961)', 4326), 'Plac Wolnica', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9410279 50.0586702)', 4326), '?i?ej Gertrudy', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9488683 50.0533232)', 4326), 'Miodowa', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9455381 50.0568776)', 4326), 'Starowi?na', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9459501 50.0571036)', 4326), 'Starowi?na', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9337881 50.0590083)', 4326), 'Filharmonia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9327223 50.0631704)', 4326), 'Teatr Bagatela', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9422700 50.0509388)', 4326), 'Stradom', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9419242 50.0520571)', 4326), 'Stradom', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9414267 50.0516047)', 4326), 'Stradom', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9448429 50.0568887)', 4326), 'Starowi?na', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9447056 50.0572909)', 4326), 'Starowi?na', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9480358 50.0538743)', 4326), 'Miodowa', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9509283 50.0510967)', 4326), '?i?ego Wawrzy?a', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9513231 50.0510636)', 4326), '?i?ego Wawrzy?a', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9411909 50.0521253)', 4326), 'Stradom', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259180 50.0695437)', 4326), 'Plac Inwalid?', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9197790 50.0719523)', 4326), 'Urz?nicza', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9194014 50.0719027)', 4326), 'Urz?nicza', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9158137 50.0730210)', 4326), 'Biprostal', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9149755 50.0730549)', 4326), 'Biprostal', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9269010 50.0688462)', 4326), 'Plac Inwalid?', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9106123 50.0742549)', 4326), 'Uniwersytet Pedagogiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9099772 50.0742329)', 4326), 'Uniwersytet Pedagogiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9328325 50.0635223)', 4326), 'Teatr Bagatela', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9330141 50.0636112)', 4326), 'Teatr Bagatela', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9292910 50.0667690)', 4326), 'Batorego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9289020 50.0669238)', 4326), 'Batorego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9475017 50.0440340)', 4326), 'Korona', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9466977 50.0436250)', 4326), 'Korona', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9433308 50.0416834)', 4326), 'Smolki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9466940 50.0440566)', 4326), 'Korona', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9548522 50.0451139)', 4326), 'Limanowskiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9561903 50.0448955)', 4326), 'Limanowskiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9330968 50.0591467)', 4326), 'Filharmonia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9329086 50.0586673)', 4326), 'Filharmonia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9383118 50.0590663)', 4326), 'Plac Wszystkich ?i?ych', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9376526 50.0590720)', 4326), 'Plac Wszystkich ?i?ych', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9387033 50.0663075)', 4326), 'Basztowa LOT', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9389328 50.0666158)', 4326), 'Basztowa LOT', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9259787 50.0346380)', 4326), 'Kobierzy?ka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9241478 50.0414899)', 4326), 'S?miana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9244701 50.0409649)', 4326), 'S?miana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9219418 50.0451159)', 4326), 'Kapelanka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9252186 50.0467167)', 4326), 'Szwedzka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9310490 50.0485980)', 4326), 'Most Grunwaldzki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9379965 50.0505192)', 4326), 'Orzeszkowej', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9387399 50.0506217)', 4326), 'Orzeszkowej', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9150371 50.0527421)', 4326), 'Salwator', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9172550 50.0526199)', 4326), 'Flisacka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9217424 50.0533630)', 4326), 'Komorowskiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9270441 50.0550182)', 4326), 'Jubilat', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9105429 50.0614228)', 4326), 'Reymana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9305740 50.0602761)', 4326), 'Uniwersytet Jagiello?ki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9314742 50.0604022)', 4326), 'Uniwersytet Jagiello?ki', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9488261 50.0586919)', 4326), 'Hala Targowa', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9667636 50.0600095)', 4326), 'Francesco Nullo', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9712247 50.0602535)', 4326), 'Fabryczna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9791564 50.0607178)', 4326), 'D?ie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9711448 50.0665741)', 4326), 'Cysters?', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9425710 50.0591765)', 4326), 'Poczta G?wna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9424868 50.0597907)', 4326), 'Poczta G?wna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9417840 50.0592887)', 4326), 'Poczta G?wna', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9450511 50.0641670)', 4326), 'Dworzec G?wny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9456405 50.0646303)', 4326), 'Dworzec G?wny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9444179 50.0647873)', 4326), 'Dworzec G?wny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9452027 50.0651756)', 4326), 'Dworzec G?wny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9510507 50.0650655)', 4326), 'Lubicz', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9516835 50.0654556)', 4326), 'Lubicz', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9522867 50.0651692)', 4326), 'Lubicz', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9522206 50.0675990)', 4326), 'Uniwersytet Ekonomiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9527224 50.0683991)', 4326), 'Uniwersytet Ekonomiczny', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9586728 50.0660293)', 4326), 'Rondo Mogilskie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9596829 50.0657246)', 4326), 'Rondo Mogilskie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9398653 50.0750830)', 4326), 'Dworzec Towarowy', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9377104 50.0807491)', 4326), 'Pr?nicka', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9336249 50.0847079)', 4326), 'Bratys?wska', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9404677 50.0759402)', 4326), 'Dworzec Towarowy', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9007314 50.0777820)', 4326), 'Bronowice', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.8977928 50.0788760)', 4326), 'Wesele', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.8966664 50.0792949)', 4326), 'Wesele', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9056435 50.0759979)', 4326), 'G?wackiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469900 50.0496600)', 4326), 'Muzeum In?nierii Miejskiej', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9376160 50.0695030)', 4326), 'P?zich?', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9453632 50.0676870)', 4326), 'Dworzec G?wny Zach? (Galeria)', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9112362 50.0613651)', 4326), 'Reymana', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9212159 50.0598187)', 4326), 'Oleandry', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9204964 50.0599080)', 4326), 'Oleandry', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9256812 50.0595416)', 4326), 'Cracovia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9243395 50.0593766)', 4326), 'Cracovia', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9469453 50.0439126)', 4326), 'Korona', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9588642 50.0571076)', 4326), 'Rondo Grzeg?zeckie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9592290 50.0582202)', 4326), 'Rondo Grzeg?zeckie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9678681 50.0487946)', 4326), 'Klimeckiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9598805 50.0578360)', 4326), 'Rondo Grzeg?zeckie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9644778 50.0498142)', 4326), 'Zab?cie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9644057 50.0499573)', 4326), 'Zab?cie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9680559 50.0488497)', 4326), 'Klimeckiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9579869 50.0576053)', 4326), 'Rondo Grzeg?zeckie', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9736188 50.0447814)', 4326), 'Kukli?kiego', 'T', 0);
INSERT INTO przystanki(id, location, nazwa, typ, version) VALUES ( nextval ('public.hibernate_sequence'), st_geomfromtext('POINT(19.9734257 50.0447022)', 4326), 'Kukli?kiego', 'T', 0);






-- LINIA  i TABLICZKI

INSERT INTO linie(id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 123, 'A', 0);

INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 18, 0, 1);
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 21,  225, 1, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 226, version=version+1 WHERE id = 225;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 25, 226, 2, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 227, version=version+1 WHERE id = 226;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 6, 227, 3, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 228, version=version+1 WHERE id = 227;
INSERT INTO przystanek_tabliczki(id, version, linia_id, przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 5, 228, 4, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 229, version=version+1 WHERE id = 228;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 11,229, 5, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 230, version=version+1 WHERE id = 229;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 14,230, 6, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 231, version=version+1 WHERE id = 230;
INSERT INTO przystanek_tabliczki(id, version, linia_id , przystanek_id, poprzedniprzystanek_id, przystanektabliczka_order, czasdonastepnego) VALUES (nextval('public.hibernate_sequence'), 0, 224, 16,231, 7, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 232, version=version+1 WHERE id = 231;

INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 3, 'T', 0); --id 233
INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 501, 'A', 0); --id 234
INSERT INTO linie (id, numer, typ, version) VALUES (nextval('public.hibernate_sequence'), 899, 'A', 0); --id 235

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 233, NULL, 121, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 233, 236, 172, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 237, version=version+1 WHERE id = 236;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 233, 237, 173, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 238, version=version+1 WHERE id = 237;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 233, 238, 174, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 239, version=version+1 WHERE id = 238;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 234, NULL, 45, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 234, 240, 50, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 241, version=version+1 WHERE id = 240;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 234, 241, 47, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 242, version=version+1 WHERE id = 241;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 234, 242, 51, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 243, version=version+1 WHERE id = 242;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 234, 243, 43, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 244, version=version+1 WHERE id = 243;

INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 2, 2, 235, NULL, 91, 0);
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 2, 2, 235, 245, 96, 1);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 246, version=version+1 WHERE id = 245;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 235, 246, 47, 2);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 247, version=version+1 WHERE id = 246;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 235, 247, 22, 3);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 248, version=version+1 WHERE id = 247;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 2, 235, 248, 60, 4);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 249, version=version+1 WHERE id = 248;
INSERT INTO przystanek_tabliczki (id, czasdonastepnego, version, linia_id, poprzedniprzystanek_id, przystanek_id, przystanektabliczka_order) VALUES (nextval('public.hibernate_sequence'), 1, 1, 235, 249, 58, 5);
UPDATE przystanek_tabliczki SET nastepnyprzystanek_id = 250, version=version+1 WHERE id = 249;



-- ODJAZDY
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:36:00', 'DZIEN_POWSZEDNI', 0, 225);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:37:00', 'DZIEN_POWSZEDNI', 0, 226);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:38:00', 'DZIEN_POWSZEDNI', 0, 227);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:39:00', 'DZIEN_POWSZEDNI', 0, 228);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:40:00', 'DZIEN_POWSZEDNI', 0, 229);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:41:00', 'DZIEN_POWSZEDNI', 0, 230);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:42:00', 'DZIEN_POWSZEDNI', 0, 231);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:43:00', 'DZIEN_POWSZEDNI', 0, 232);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:22:00', 'SWIETA', 1, 225);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:23:00', 'SWIETA', 1, 226);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:24:00', 'SWIETA', 1, 227);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:25:00', 'SWIETA', 1, 228);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:26:00', 'SWIETA', 1, 229);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:27:00', 'SWIETA', 1, 230);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:28:00', 'SWIETA', 1, 231);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:29:00', 'SWIETA', 1, 232);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:31:00', 'DZIEN_POWSZEDNI', 0, 236);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:30:00', 'SWIETA', 0, 236);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:32:00', 'DZIEN_POWSZEDNI', 0, 237);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:31:00', 'SWIETA', 0, 237);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:33:00', 'DZIEN_POWSZEDNI', 0, 238);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:32:00', 'SWIETA', 0, 238);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:34:00', 'DZIEN_POWSZEDNI', 0, 239);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '19:33:00', 'SWIETA', 0, 239);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:16.112', 'DZIEN_POWSZEDNI', 0, 240);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:17.647', 'SWIETA', 0, 240);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:04:16.112', 'DZIEN_POWSZEDNI', 0, 241);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:04:17.647', 'SWIETA', 0, 241);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:16.112', 'DZIEN_POWSZEDNI', 0, 242);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:17.647', 'SWIETA', 0, 242);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:06:16.112', 'DZIEN_POWSZEDNI', 0, 243);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:06:17.647', 'SWIETA', 0, 243);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:16.112', 'DZIEN_POWSZEDNI', 0, 244);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:17.647', 'SWIETA', 0, 244);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'DZIEN_POWSZEDNI', 0, 245);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:03:00', 'SWIETA', 0, 245);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'DZIEN_POWSZEDNI', 0, 246);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:05:00', 'SWIETA', 0, 246);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'DZIEN_POWSZEDNI', 0, 247);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:07:00', 'SWIETA', 0, 247);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:08:00', 'DZIEN_POWSZEDNI', 0, 248);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:08:00', 'SWIETA', 0, 248);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:09:00', 'DZIEN_POWSZEDNI', 0, 249);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:09:00', 'SWIETA', 0, 249);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:10:00', 'DZIEN_POWSZEDNI', 0, 250);
INSERT INTO odjazdy (id, czas, typdnia, version, przystanektabliczka_id) VALUES (nextval('public.hibernate_sequence'), '21:10:00', 'SWIETA', 0, 250);


-- KONFIGURACJA

INSERT INTO konfiguracja(id, liczbawatkow, name, nieskonczonosc, odlegloscprzystankow, odlegloscdostartstop, predkoscpasazera, version) VALUES (nextval('public.hibernate_sequence'), 0, 'default', 9999, 200, 500, 6.0, 0)

