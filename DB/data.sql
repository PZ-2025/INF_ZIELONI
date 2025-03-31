INSERT INTO roles (name) VALUES ('Admin'), ('Dyrektor'), ('Kierownik'), ('Pracownik');

INSERT INTO departments (name) VALUES ('Roślinność zewnętrzna'), ('Roślinność wewnętrzna'), ('Budownictwo');

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
('pracownik6@com', 'password', 'Karol', 'Kowal', 3, 4);

INSERT INTO tasks (title, description, user_id, status, deadline)
VALUES 
('Wyczysz podłogi', 'Jest syf.', 4, 'pending', '2025-04-01'),
('Uporządkuj magazyn', 'Brud na magazynie.', 4, 'in progress', '2025-03-30'),
('Uzupełnij półki', 'Puste półki.', 5, 'completed', '2025-03-15');

INSERT INTO warehouses (department_id, name) 
VALUES 
(1, 'Magazyn A - Roślinność zewnętrzna'),
(2, 'Magazyn B - Roślinność wewnętrzna'),
(3, 'Magazyn C - Budownictwo');

INSERT INTO items (warehouse_id, name, quantity, description)
VALUES 
(1, 'Nasiona trawy', 100, 'Nasiona do trawników zewnętrznych.'),
(1, 'Łopata', 10, 'Łopata ogrodnicza.'),
(2, 'Doniczki', 50, 'Plastikowe doniczki do roślin wewnętrznych.'),
(2, 'Ziemia kwiatowa', 200, 'Specjalna mieszanka ziemi do kwiatów.'),
(3, 'Cement', 30, 'Worki z cementem 25kg.'),
(3, 'Młotek', 15, 'Młotek budowlany.');
