DELETE FROM users;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

INSERT INTO users (login, password, role)
VALUES ('admin', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'ADMIN'),
       ('user', '$2a$10$n7XwQKXbba5XWv91/7O61OXY2TW3IUPvAtVo7GZVWQgLXQncxXSsC', 'USER');
