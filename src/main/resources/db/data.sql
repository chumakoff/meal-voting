-- Users
ALTER SEQUENCE users_id_seq RESTART WITH 1;
DELETE FROM users;
INSERT INTO users (login, password, role)
VALUES ('admin', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'ADMIN'),
       ('user1', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'USER'),
       ('user2', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'USER'),
       ('test-user1', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'USER');

-- Restaurant
ALTER SEQUENCE restaurant_id_seq RESTART WITH 1;
DELETE FROM restaurant;
INSERT INTO restaurant (name)
VALUES ('Старик Хинкалыч'), ('Теремок'), ('Мансарда'), ('TERRASSA');

-- Menu
ALTER SEQUENCE menu_id_seq RESTART WITH 1;
DELETE FROM menu;
INSERT INTO menu (restaurant_id, menu_date, dishes)
VALUES (1, CURRENT_DATE() - 1, JSON '[{"name":"Dish 1","price":1000},{"name":"Dish 2","price":1000},{"name":"Dish 3","price":1000}]'),
       (1, CURRENT_DATE() + 1, JSON '[{"name":"Dish 1","price":1000},{"name":"Dish 2","price":1000},{"name":"Dish 3","price":1000}]'),
       (1, CURRENT_DATE(), JSON '[{"name":"Today Dish 1","price":1000},{"name":"Today Dish 2","price":1000},{"name":"Today Dish 3","price":1000}]'),
       (2, CURRENT_DATE(), JSON '[{"name":"Today Dish 1","price":1000},{"name":"Today Dish 2","price":1000},{"name":"Today Dish 3","price":1000}]'),
       (3, CURRENT_DATE(), JSON '[{"name":"Today Dish 1","price":1000},{"name":"Today Dish 2","price":1000},{"name":"Today Dish 3","price":1000}]');

-- Vote
ALTER SEQUENCE vote_id_seq RESTART WITH 1;
DELETE FROM vote;
INSERT INTO vote (user_id, restaurant_id, meal_date)
VALUES (2, 1,CURRENT_DATE() - 1),
       (2, 2, CURRENT_DATE() + 1),
       (2, 3, CURRENT_DATE()),
       (3, 1, CURRENT_DATE()),
       (3, 2, CURRENT_DATE() + 1);
