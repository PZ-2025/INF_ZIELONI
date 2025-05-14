INSERT INTO roles (name) VALUES
('Admin'),
('Dyrektor'),
('Kierownik'),
('Pracownik');


INSERT INTO departments (name) VALUES 
('Roslinnosc zewnetrzna'), 
('Roslinnosc wewnetrzna'), 
('Sprzet budowlany'), 
('Sprzet ogrodniczy'), 
('Pielegnacja'), 
('Artykuly dekoracyjne'), 
('Warzywnictwo'),
('Narzedzia reczne'),
('Oswietlenie ogrodowe'),
('Transport i logistyka'),
('Systemy nawadniania'),
('Chemia ogrodowa'),
('Strefa klienta');


INSERT INTO users (email, password, first_name, last_name, city, salary, department_id, role_id) VALUES 
('admin@admin', 'password', 'Admin', 'Admin', 'Warszawa', 15000.45, NULL, 1),
('dyrektor@com', 'password', 'Joanna', 'Malinowska', 'Krakow', 13000.88, NULL, 2),
('kierownik@com', 'password', 'Elzbieta', 'Zielinska', 'Gdansk', 10000.99, 1, 3),
('pracownik1@com', 'password', 'Pawel', 'Nowak', 'Łodz', 7000.00, 1, 4),
('pracownik2@com', 'password', 'Michal', 'Wisniewski', 'Wroclaw', 7000.00, 1, 4),
('kierownik2@com', 'password', 'Katarzyna', 'Kowalczyk', 'Poznan', 10000.99, 2, 3),
('pracownik3@com', 'password', 'Tomasz', 'Kaminski', 'Szczecin', 7000.00, 2, 4),
('pracownik4@com', 'password', 'Rafal', 'Lewandowski', 'Lublin', 7000.00, 2, 4),
('kierownik3@com', 'password', 'Magdalena', 'Wojcik', 'Katowice', 10000.99, 3, 3),
('pracownik5@com', 'password', 'Lukasz', 'Kaczmarek', 'Bydgoszcz', 7000.00, 3, 4),
('pracownik6@com', 'password', 'Marcin', 'Piotrowski', 'Rzeszow', 7000.00, 3, 4),
('kierownik4@com', 'password', 'Beata', 'Dabrowska', 'Bialystok', 10000.99, 4, 3),
('pracownik7@com', 'password', 'Patryk', 'Szymanski', 'Gdynia', 7000.00, 4, 4),
('pracownik8@com', 'password', 'Jakub', 'Wojciechowski', 'Opole', 7000.00, 4, 4),
('kierownik5@com', 'password', 'Aneta', 'Kubiak', 'Torun', 10000.99, 5, 3),
('pracownik9@com', 'password', 'Dawid', 'Lis', 'Olsztyn', 7000.00, 5, 4),
('pracownik10@com', 'password', 'Wojciech', 'Mazur', 'Kielce', 7000.00, 5, 4),
('kierownik6@com', 'password', 'Renata', 'Kaczmarczyk', 'Radom', 10000.99, 6, 3),
('pracownik11@com', 'password', 'Mateusz', 'Jankowski', 'Zielona Gora', 7000.00, 6, 4),
('pracownik12@com', 'password', 'Sebastian', 'Olszewski', 'Siedlce', 7000.00, 6, 4),
('kierownik7@com', 'password', 'Urszula', 'Sikora', 'Koszalin', 10000.99, 7, 3),
('pracownik13@com', 'password', 'Adrian', 'Gorski', 'Pila', 7000.00, 7, 4),
('pracownik14@com', 'password', 'Bartosz', 'Pawlak', 'Leszno', 7000.00, 7, 4),
('kierownik8@com', 'password', 'Ewelina', 'Grabowska', 'Czestochowa', 10000.99, 8, 3),
('pracownik15@com', 'password', 'Grzegorz', 'Bak', 'Jelenia Gora', 7000.00, 8, 4),
('pracownik16@com', 'password', 'Karina', 'Wrobel', 'Nowy Sacz', 7000.00, 8, 4),
('kierownik9@com', 'password', 'Mariusz', 'Krol', 'Walbrzych', 10000.99, 9, 3),
('pracownik17@com', 'password', 'Lena', 'Czarnecka', 'Tarnow', 7000.00, 9, 4),
('pracownik18@com', 'password', 'Dominik', 'Sosnowski', 'Przemysl', 7000.00, 9, 4),
('kierownik10@com', 'password', 'Natalia', 'Wasilewska', 'Elblag', 10000.99, 10, 3),
('pracownik19@com', 'password', 'Zofia', 'Baran', 'Plock', 7000.00, 10, 4),
('pracownik20@com', 'password', 'Marek', 'Stanek', 'Zamosc', 7000.00, 10, 4),
('kierownik11@com', 'password', 'Tadeusz', 'Borkowski', 'Chelm', 10000.99, 11, 3),
('pracownik21@com', 'password', 'Joanna', 'Michalak', 'Legnica', 7000.00, 11, 4),
('pracownik22@com', 'password', 'Mikolaj', 'Gajda', 'Gorzow Wielkopolski', 7000.00, 11, 4),
('kierownik12@com', 'password', 'Edyta', 'Maj', 'Slupsk', 10000.99, 12, 3),
('pracownik23@com', 'password', 'Izabela', 'Szewczyk', 'Tczew', 7000.00, 12, 4),
('pracownik24@com', 'password', 'Przemyslaw', 'Walczak', 'Elk', 7000.00, 12, 4),
('kierownik13@com', 'password', 'Bartlomiej', 'Tomczyk', 'Ostroleka', 10000.99, 13, 3),
('pracownik25@com', 'password', 'Alicja', 'Markowska', 'Starachowice', 7000.00, 13, 4),
('pracownik26@com', 'password', 'Kamil', 'Jablonski', 'Konin', 7000.00, 13, 4);


UPDATE departments SET manager_id = 3 where id = 1;
UPDATE departments SET manager_id = 6 where id = 2;
UPDATE departments SET manager_id = 9 where id = 3;
UPDATE departments SET manager_id = 12 where id = 4;
UPDATE departments SET manager_id = 15 where id = 5;
UPDATE departments SET manager_id = 18 where id = 6;
UPDATE departments SET manager_id = 21 where id = 7;
UPDATE departments SET manager_id = 24 WHERE id = 8;
UPDATE departments SET manager_id = 27 WHERE id = 9;
UPDATE departments SET manager_id = 30 WHERE id = 10;
UPDATE departments SET manager_id = 33 WHERE id = 11;
UPDATE departments SET manager_id = 36 WHERE id = 12;
UPDATE departments SET manager_id = 39 WHERE id = 13;


INSERT INTO tasks (title, description, user_id, status, deadline, priority) VALUES
('Wyczysc podlogi', 'Na podlodze panuje balagan.', 4, 'Nie rozpoczete', '2025-05-07', 'Wysoki'),
('Uporzadkuj magazyn', 'Magazyn wymaga uporzadkowania.', 4, 'W trakcie', '2025-05-30', 'Sredni'),
('Uzupelnij polki', 'Na polkach brakuje towaru.', 5, 'Zakonczone', '2025-05-15', 'Wysoki'),
('Zorganizuj narzedzia', 'Narzedzia sa w nieladzie – uporzadkuj je.', 7, 'Nie rozpoczete', '2025-05-08', 'Sredni'),
('Sprawdz zapasy cementu', 'Zrob inwentaryzacje cementu.', 7, 'W trakcie', '2025-05-10', 'Wysoki'),
('Przygotuj pojazdy', 'Pojazdy wymagaja przegladu przed sezonem.', 8, 'Oczekujace', '2025-05-15', 'Sredni'),
('Zainstaluj nowe lampy', 'Zamontuj nowe oswietlenie w magazynie.', 8, 'W trakcie', '2025-05-20', 'Niski'),
('Rozladuj dostawe', 'Rozpakuj nowa dostawe narzedzi i poukladaj je na polkach.', 7, 'Oczekujace', '2025-05-12', 'Sredni'),
('Posprzataj magazyn', 'Usun opakowania i zamiec podłoge.', 13, 'W trakcie', '2025-05-16', 'Niski'),
('Skompletuj zamowienie', 'Przygotuj paczki dla klientow.', 17, 'Nie rozpoczete', '2025-05-14', 'Wysoki'),
('Sprawdz stan zapasow', 'Zweryfikuj ilosci przedmiotow i zglos braki.', 19, 'Zakonczone', '2025-05-28', 'Wysoki'),
('Przejrzyj narzedzia', 'Sprawdz stan techniczny wszystkich narzedzi.', 25, 'Oczekujace', '2025-05-18', 'Sredni'),
('Zainstaluj lampy solarne', 'Montaz lamp w ogrodzie pokazowym.', 28, 'W trakcie', '2025-05-22', 'Niski'),
('Zorganizuj transport', 'Przygotuj harmonogram dostaw.', 31, 'Oczekujace', '2025-05-25', 'Sredni'),
('Wymien zuzyte narzedzia', 'Usun uszkodzone narzedzia i zglos zapotrzebowanie.', 26, 'Zakonczone', '2025-05-10', 'Wysoki'),
('Przygotuj oswietlenie ekspozycyjne', 'Zadbaj o montaz lamp LED na wystawie.', 29, 'Oczekujace', '2025-05-23', 'Sredni'),
('Sprawdź stan pojazdow', 'Skontroluj stan techniczny wozkow transportowych.', 32, 'W trakcie', '2025-05-20', 'Sredni'),
('Zamontuj system kropelkowy', 'Instalacja systemu nawadniania w szklarni.', 34, 'Nie rozpoczete', '2025-05-27', 'Wysoki'),
('Przeprowadz test opryskow', 'Testuj nowy srodek przeciwko mszycom.', 37, 'W trakcie', '2025-05-11', 'Wysoki'),
('Zorganizuj punkt informacyjny', 'Stworz nowe stanowisko dla klientow.', 40, 'Oczekujace', '2025-05-29', 'Sredni'),
('Zaplanuj promocje nawozow', 'Stworz kampanie informacyjna dla klientow.', 38, 'Zakonczone', '2025-05-01', 'Niski'),
('Wydrukuj broszury', 'Przygotuj materialy reklamowe dla strefy klienta.', 41, 'Nie rozpoczete', '2025-05-09', 'Sredni'),
('Zbadaj zapotrzebowanie na pestycydy', 'Analiza zakupow i zuzycia.', 37, 'W trakcie', '2025-05-12', 'Wysoki');


INSERT INTO warehouses (department_id, name) VALUES 
(1, 'Magazyn A – Roslinnosc zewnetrzna'),
(2, 'Magazyn B – Roslinnosc wewnetrzna'),
(3, 'Magazyn C – Sprzet budowlany'),
(4, 'Magazyn D – Sprzet ogrodniczy'),
(5, 'Magazyn E – Pielegnacja'),
(6, 'Magazyn F – Artykuly dekoracyjne'),
(7, 'Magazyn G – Warzywnictwo'),
(8, 'Magazyn H – Narzedzia reczne'),
(9, 'Magazyn I – Oswietlenie ogrodowe'),
(10, 'Magazyn J – Transport i logistyka'),
(11, 'Magazyn K – Systemy nawadniania'),
(12, 'Magazyn L – Chemia ogrodowa'),
(13, 'Magazyn M - Strefa klienta');


INSERT INTO items (warehouse_id, name, quantity, description) VALUES 
(1, 'Nasiona trawy', 100, 'Mieszanka traw do ogrodow zewnetrznych.'),
(1, 'Lopata', 10, 'Lopata ogrodowa metalowa.'),
(2, 'Doniczki', 50, 'Plastikowe doniczki na kwiaty domowe.'),
(2, 'Ziemia do kwiatow', 200, 'Mieszanka ziemi do roslin domowych.'),
(3, 'Cement', 30, 'Worki 25 kg z cementem budowlanym.'),
(3, 'Mlotek', 15, 'Mlotek do prac budowlanych.'),
(1, 'Trawa ogrodowa', 120, 'Specjalna mieszanka do ogrodow.'),
(2, 'Konewka', 30, 'Konewka metalowa o pojemnosci 10 litrow.'),
(3, 'Gwozdzie 10 cm', 200, 'Gwozdzie budowlane w paczce.'),
(4, 'Pila spalinowa', 8, 'Profesjonalna pila do drewna.'),
(5, 'Sekator', 15, 'Sekator do przycinania krzewow.'),
(6, 'Figurka ogrodowa', 50, 'Dekoracyjna figurka krasnala.'),
(7, 'Nawoz do warzyw', 75, 'Ekologiczny nawoz do roslin warzywnych.'),
(8, 'Klucz francuski', 25, 'Klucz regulowany do prac serwisowych.'),
(8, 'Wkretaki', 60, 'Zestaw wkretakow plaskich i krzyzakowych.'),
(9, 'Lampa solarna', 40, 'Lampa ogrodowa z panelem slonecznym.'),
(9, 'Girlanda LED', 20, 'Dekoracyjna girlanda swietlna na taras.'),
(10, 'Wozek transportowy', 10, 'Wozek reczny do przewozu towarow.'),
(10, 'Paleta drewniana', 80, 'Standardowa paleta do magazynowania.'),
(11, 'Rura nawadniajaca', 100, 'Elastyczna rura do systemow kropelkowych.'),
(11, 'Sterownik do nawadniania', 20, 'Automatyczny sterownik z wyswietlaczem LCD.'),
(12, 'Srodek chwastobojczy', 60, 'Plynny koncentrat na chwasty.'),
(12, 'Nawoz uniwersalny', 80, 'Wieloskladnikowy nawoz granulowany.'),
(12, 'Srodek przeciw mszycom', 50, 'Preparat owadobojczy w sprayu.'),
(13, 'Ulotki promocyjne', 500, 'Broszury informacyjne dla klientow.'),
(13, 'Stojak na ulotki', 10, 'Metalowy stojak na materialy reklamowe.'),
(13, 'Zestaw powitalny klienta', 30, 'Torebka z probkami nawozow i katalogiem.');