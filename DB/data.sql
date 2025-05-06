INSERT INTO roles (name) VALUES
('Admin'),
('Dyrektor'),
('Kierownik'),
('Pracownik');


INSERT INTO departments (name) VALUES 
('Roślinność zewnętrzna'), 
('Roślinność wewnętrzna'), 
('Sprzęt budowlany'), 
('Sprzęt ogrodniczy'), 
('Pielęgnacja'), 
('Artykuły dekoracyjne'), 
('Warzywnictwo'),
('Narzędzia ręczne'),
('Oświetlenie ogrodowe'),
('Transport i logistyka'),
('Systemy nawadniania'),
('Chemia ogrodowa'),
('Strefa klienta');


INSERT INTO users (email, password, first_name, last_name, department_id, role_id) VALUES 
('admin@admin', 'password', 'Admin', 'Admin', NULL, 1),
('dyrektor@com', 'password', 'Joanna', 'Malinowska', NULL, 2),
('kierownik@com', 'password', 'Elżbieta', 'Zielińska', 1, 3),
('pracownik1@com', 'password', 'Paweł', 'Nowak', 1, 4),
('pracownik2@com', 'password', 'Michał', 'Wiśniewski', 1, 4),
('kierownik2@com', 'password', 'Katarzyna', 'Kowalczyk', 2, 3),
('pracownik3@com', 'password', 'Tomasz', 'Kamiński', 2, 4),
('pracownik4@com', 'password', 'Rafał', 'Lewandowski', 2, 4),
('kierownik3@com', 'password', 'Magdalena', 'Wójcik', 3, 3),
('pracownik5@com', 'password', 'Łukasz', 'Kaczmarek', 3, 4),
('pracownik6@com', 'password', 'Marcin', 'Piotrowski', 3, 4),
('kierownik4@com', 'password', 'Beata', 'Dąbrowska', 4, 3),
('pracownik7@com', 'password', 'Patryk', 'Szymański', 4, 4),
('pracownik8@com', 'password', 'Jakub', 'Wojciechowski', 4, 4),
('kierownik5@com', 'password', 'Aneta', 'Kubiak', 5, 3),
('pracownik9@com', 'password', 'Dawid', 'Lis', 5, 4),
('pracownik10@com', 'password', 'Wojciech', 'Mazur', 5, 4),
('kierownik6@com', 'password', 'Renata', 'Kaczmarczyk', 6, 3),
('pracownik11@com', 'password', 'Mateusz', 'Jankowski', 6, 4),
('pracownik12@com', 'password', 'Sebastian', 'Olszewski', 6, 4),
('kierownik7@com', 'password', 'Urszula', 'Sikora', 7, 3),
('pracownik13@com', 'password', 'Adrian', 'Górski', 7, 4),
('pracownik14@com', 'password', 'Bartosz', 'Pawlak', 7, 4),
('kierownik8@com', 'password', 'Ewelina', 'Grabowska', 8, 3),
('pracownik15@com', 'password', 'Grzegorz', 'Bąk', 8, 4),
('pracownik16@com', 'password', 'Karina', 'Wróbel', 8, 4),
('kierownik9@com', 'password', 'Mariusz', 'Król', 9, 3),
('pracownik17@com', 'password', 'Lena', 'Czarnecka', 9, 4),
('pracownik18@com', 'password', 'Dominik', 'Sosnowski', 9, 4),
('kierownik10@com', 'password', 'Natalia', 'Wasilewska', 10, 3),
('pracownik19@com', 'password', 'Zofia', 'Baran', 10, 4),
('pracownik20@com', 'password', 'Marek', 'Stanek', 10, 4),
('kierownik11@com', 'password', 'Tadeusz', 'Borkowski', 11, 3),
('pracownik21@com', 'password', 'Joanna', 'Michalak', 11, 4),
('pracownik22@com', 'password', 'Mikołaj', 'Gajda', 11, 4),
('kierownik12@com', 'password', 'Edyta', 'Maj', 12, 3),
('pracownik23@com', 'password', 'Izabela', 'Szewczyk', 12, 4),
('pracownik24@com', 'password', 'Przemysław', 'Walczak', 12, 4),
('kierownik13@com', 'password', 'Bartłomiej', 'Tomczyk', 13, 3),
('pracownik25@com', 'password', 'Alicja', 'Markowska', 13, 4),
('pracownik26@com', 'password', 'Kamil', 'Jabłoński', 13, 4);


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


INSERT INTO tasks (title, description, user_id, status, deadline) VALUES 
('Wyczyść podłogi', 'Na podłodze panuje bałagan.', 4, 'Nie rozpoczęte', '2025-05-07'),
('Uporządkuj magazyn', 'Magazyn wymaga uporządkowania.', 4, 'W trakcie', '2025-05-30'),
('Uzupełnij półki', 'Na półkach brakuje towaru.', 5, 'Zakończone', '2025-05-15'),
('Zorganizuj narzędzia', 'Narzędzia są w nieładzie – uporządkuj je.', 7, 'Nie rozpoczęte', '2025-05-08'),
('Sprawdź zapasy cementu', 'Zrób inwentaryzację cementu.', 7, 'W trakcie', '2025-05-10'),
('Przygotuj pojazdy', 'Pojazdy wymagają przeglądu przed sezonem.', 8, 'Oczekujące', '2025-05-15'),
('Zainstaluj nowe lampy', 'Zamontuj nowe oświetlenie w magazynie.', 8, 'W trakcie', '2025-05-20'),
('Rozładuj dostawę', 'Rozpakuj nową dostawę narzędzi i poukładaj je na półkach.', 7, 'Oczekujące', '2025-05-12'),
('Posprzątaj magazyn', 'Usuń opakowania i zamieć podłogę.', 13, 'W trakcie', '2025-05-016'),
('Skompletuj zamówienie', 'Przygotuj paczki dla klientów.', 17, 'Nie rozpoczęte', '2025-05-14'),
('Sprawdź stan zapasów', 'Zweryfikuj ilości przedmiotów i zgłoś braki.', 19, 'Zakończone', '2025-05-28'),
('Przejrzyj narzędzia', 'Sprawdź stan techniczny wszystkich narzędzi.', 25, 'Oczekujące', '2025-05-18'),
('Zainstaluj lampy solarne', 'Montaż lamp w ogrodzie pokazowym.', 28, 'W trakcie', '2025-05-22'),
('Zorganizuj transport', 'Przygotuj harmonogram dostaw.', 31, 'Oczekujące', '2025-05-25'),
('Wymień zużyte narzędzia', 'Usuń uszkodzone narzędzia i zgłoś zapotrzebowanie.', 26, 'Zakończone', '2025-05-10'),
('Przygotuj oświetlenie ekspozycyjne', 'Zadbaj o montaż lamp LED na wystawie.', 29, 'Oczekujące', '2025-05-23'),
('Sprawdź stan pojazdów', 'Skontroluj stan techniczny wózków transportowych.', 32, 'W trakcie', '2025-05-20'),
('Zamontuj system kropelkowy', 'Instalacja systemu nawadniania w szklarni.', 34, 'Nie rozpoczęte', '2025-05-27'),
('Przeprowadź test oprysków', 'Testuj nowy środek przeciwko mszycom.', 37, 'W trakcie', '2025-05-11'),
('Zorganizuj punkt informacyjny', 'Stwórz nowe stanowisko dla klientów.', 40, 'Oczekujące', '2025-05-29'),
('Zaplanuj promocję nawozów', 'Stwórz kampanię informacyjną dla klientów.', 38, 'Zakończone', '2025-05-01'),
('Wydrukuj broszury', 'Przygotuj materiały reklamowe dla strefy klienta.', 41, 'Nie rozpoczęte', '2025-05-09'),
('Zbadaj zapotrzebowanie na pestycydy', 'Analiza zakupów i zużycia.', 37, 'W trakcie', '2025-05-12');


INSERT INTO warehouses (department_id, name) VALUES 
(1, 'Magazyn A – Roślinność zewnętrzna'),
(2, 'Magazyn B – Roślinność wewnętrzna'),
(3, 'Magazyn C – Sprzęt budowlany'),
(4, 'Magazyn D – Sprzęt ogrodniczy'),
(5, 'Magazyn E – Pielęgnacja'),
(6, 'Magazyn F – Artykuły dekoracyjne'),
(7, 'Magazyn G – Warzywnictwo'),
(8, 'Magazyn H – Narzędzia ręczne'),
(9, 'Magazyn I – Oświetlenie ogrodowe'),
(10, 'Magazyn J – Transport i logistyka'),
(11, 'Magazyn K – Systemy nawadniania'),
(12, 'Magazyn L – Chemia ogrodowa'),
(13, 'Magazyn M - Strefa klienta');


INSERT INTO items (warehouse_id, name, quantity, description) VALUES 
(1, 'Nasiona trawy', 100, 'Mieszanka traw do ogrodów zewnętrznych.'),
(1, 'Łopata', 10, 'Łopata ogrodowa metalowa.'),
(2, 'Doniczki', 50, 'Plastikowe doniczki na kwiaty domowe.'),
(2, 'Ziemia do kwiatów', 200, 'Mieszanka ziemi do roślin domowych.'),
(3, 'Cement', 30, 'Worki 25 kg z cementem budowlanym.'),
(3, 'Młotek', 15, 'Młotek do prac budowlanych.'),
(1, 'Trawa ogrodowa', 120, 'Specjalna mieszanka do ogrodów.'),
(2, 'Konewka', 30, 'Konewka metalowa o pojemności 10 litrów.'),
(3, 'Gwoździe 10 cm', 200, 'Gwoździe budowlane w paczce.'),
(4, 'Piła spalinowa', 8, 'Profesjonalna piła do drewna.'),
(5, 'Sekator', 15, 'Sekator do przycinania krzewów.'),
(6, 'Figurka ogrodowa', 50, 'Dekoracyjna figurka krasnala.'),
(7, 'Nawóz do warzyw', 75, 'Ekologiczny nawóz do roślin warzywnych.'),
(8, 'Klucz francuski', 25, 'Klucz regulowany do prac serwisowych.'),
(8, 'Wkrętaki', 60, 'Zestaw wkrętaków płaskich i krzyżakowych.'),
(9, 'Lampa solarna', 40, 'Lampa ogrodowa z panelem słonecznym.'),
(9, 'Girlanda LED', 20, 'Dekoracyjna girlanda świetlna na taras.'),
(10, 'Wózek transportowy', 10, 'Wózek ręczny do przewozu towarów.'),
(10, 'Paleta drewniana', 80, 'Standardowa paleta do magazynowania.'),
(11, 'Rura nawadniająca', 100, 'Elastyczna rura do systemów kropelkowych.'),
(11, 'Sterownik do nawadniania', 20, 'Automatyczny sterownik z wyświetlaczem LCD.'),
(12, 'Środek chwastobójczy', 60, 'Płynny koncentrat na chwasty.'),
(12, 'Nawóz uniwersalny', 80, 'Wieloskładnikowy nawóz granulowany.'),
(12, 'Środek przeciw mszycom', 50, 'Preparat owadobójczy w sprayu.'),
(13, 'Ulotki promocyjne', 500, 'Broszury informacyjne dla klientów.'),
(13, 'Stojak na ulotki', 10, 'Metalowy stojak na materiały reklamowe.'),
(13, 'Zestaw powitalny klienta', 30, 'Torebka z próbkami nawozów i katalogiem.');