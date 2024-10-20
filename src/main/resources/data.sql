-- This file will be executed after schema.sql to insert initial data

INSERT INTO users (username, password, enabled)
VALUES 
('user', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true),
('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true);

INSERT INTO authorities (username, authority)
VALUES 
('user', 'ROLE_USER'),
('admin', 'ROLE_ADMIN');