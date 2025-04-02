INSERT INTO roles (name) VALUES ('Admin'), ('Dyrektor'), ('Kierownik'), ('Pracownik');


INSERT INTO departments (name) VALUES ('Roślinność zewnętrzna'), ('Roślinność wewnętrzna'), 
            ('Sprzęt budowniczy'), ('Sprzęt ogrodniczy'), ('Pielegnacja'), 
            ('Artykuły dekoracyjne'), ('Warzywnictwo');


INSERT INTO users (email, password, first_name, last_name, department_id, role_id)
VALUES 
('admin@admin', 'password', 'admin', 'admin', NULL, 1),
('dyrektor@com', 'password', 'Jane', 'Smith', NULL, 2),
('kierownik@com', 'password', 'Alice', 'Brown', 1, 3),
('pracownik1@com', 'password', 'Janek', 'Nowak', 1, 4),
('pracownik2@com', 'password', 'Karol', 'Kowal', 1, 4),
('kierownik2@com', 'password', 'Alice', 'Brown', 2, 3),
('pracownik3@com', 'password', 'Janek', 'Nowak', 2, 4),
('pracownik4@com', 'password', 'Karol', 'Kowal', 2, 4),
('kierownik3@com', 'password', 'Alice', 'Brown', 3, 3),
('pracownik5@com', 'password', 'Janek', 'Nowak', 3, 4),
('pracownik6@com', 'password', 'Karol', 'Kowal', 3, 4),
('kierownik4@com', 'password', 'Alice', 'Brown', 4, 3),
('pracownik7@com', 'password', 'Janek', 'Nowak', 4, 4),
('pracownik8@com', 'password', 'Karol', 'Kowal', 4, 4),
('kierownik5@com', 'password', 'Alice', 'Brown', 5, 3),
('pracownik9@com', 'password', 'Janek', 'Nowak', 5, 4),
('pracownik10@com', 'password', 'Karol', 'Kowal', 5, 4),
('kierownik6@com', 'password', 'Alice', 'Brown', 6, 3),
('pracownik11@com', 'password', 'Janek', 'Nowak', 6, 4),
('pracownik12@com', 'password', 'Karol', 'Kowal', 6, 4),
('kierownik7@com', 'password', 'Alice', 'Brown', 7, 3),
('pracownik13@com', 'password', 'Janek', 'Nowak', 7, 4),
('pracownik14@com', 'password', 'Karol', 'Kowal', 7, 4);

UPDATE departments SET manager_id = 3 where id = 1;
UPDATE departments SET manager_id = 6 where id = 2;
UPDATE departments SET manager_id = 9 where id = 3;
UPDATE departments SET manager_id = 12 where id = 4;
UPDATE departments SET manager_id = 15 where id = 5;
UPDATE departments SET manager_id = 18 where id = 6;
UPDATE departments SET manager_id = 21 where id = 7;

INSERT INTO tasks (title, description, user_id, status, deadline)
VALUES 
('Wyczysz podłogi', 'Jest syf.', 4, 'pending', '2025-04-01'),
('Uporządkuj magazyn', 'Brud na magazynie.', 4, 'in progress', '2025-03-30'),
('Uzupełnij półki', 'Puste półki.', 5, 'completed', '2025-03-15'),
('Zorganizuj narzędzia', 'Narzędzia są w nieładzie, uporządkuj je.', 7, 'pending', '2025-04-05'),
('Sprawdź zapasy cementu', 'Należy zrobić inwentaryzację cementu.', 7, 'in progress', '2025-04-10'),
('Przygotuj pojazdy', 'Pojazdy wymagają przeglądu przed sezonem.', 8, 'pending', '2025-04-15'),
('Zainstaluj nowe lampy', 'Nowe oświetlenie w magazynie wymaga montażu.', 8, 'in progress', '2025-04-20'),
('Rozładuj dostawę', 'Nowa dostawa narzędzi, rozpakuj i umieść na półkach.', 7, 'pending', '2025-04-02'),
('Posprzątaj magazyn', 'Usuń niepotrzebne opakowania i zamiataj podłogę.', 13, 'in progress', '2025-04-05'),
('Skompletuj zamówienie', 'Przygotuj paczki do wysyłki dla klientów.', 17, 'pending', '2025-04-08'),
('Sprawdź stan zapasów', 'Zweryfikuj ilość dostępnych przedmiotów i zgłoś braki.', 19, 'completed', '2025-03-28');

INSERT INTO warehouses (department_id, name) 
VALUES 
(1, 'Magazyn A - Roślinność zewnętrzna'),
(2, 'Magazyn B - Roślinność wewnętrzna'),
(3, 'Magazyn C - Budownictwo'),
(4, 'Magazyn D - Sprzęt ogrodniczy'),
(5, 'Magazyn E - Pielegnacja'),
(6, 'Magazyn F - Artykuły dekoracyjne'),
(7, 'Magazyn G - Warzywnictwo');

INSERT INTO items (warehouse_id, name, quantity, description)
VALUES 
(1, 'Nasiona trawy', 100, 'Nasiona do trawników zewnętrznych.'),
(1, 'Łopata', 10, 'Łopata ogrodnicza.'),
(2, 'Doniczki', 50, 'Plastikowe doniczki do roślin wewnętrznych.'),
(2, 'Ziemia kwiatowa', 200, 'Specjalna mieszanka ziemi do kwiatów.'),
(3, 'Cement', 30, 'Worki z cementem 25kg.'),
(3, 'Młotek', 15, 'Młotek budowlany.'),
(1, 'Trawa ogrodowa', 120, 'Specjalna mieszanka trawy do ogrodów.'),
(2, 'Konewka', 30, 'Metalowa konewka 10L.'),
(3, 'Gwoździe 10cm', 200, 'Paczka gwoździ budowlanych.'),
(4, 'Piła spalinowa', 8, 'Profesjonalna piła do drewna.'),
(5, 'Sekator', 15, 'Sekator ogrodowy do cięcia krzewów.'),
(6, 'Figurka ogrodowa', 50, 'Dekoracyjna figurka krasnala.'),
(7, 'Nawóz do warzyw', 75, 'Ekologiczny nawóz dla roślin warzywnych.');
