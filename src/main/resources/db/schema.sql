-- Users
CREATE SEQUENCE IF NOT EXISTS users_id_seq AS BIGINT START WITH 1;
CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT DEFAULT NEXT VALUE FOR users_id_seq NOT NULL PRIMARY KEY,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS index_users_on_login ON users (login);

-- Restaurant
CREATE SEQUENCE IF NOT EXISTS restaurant_id_seq AS BIGINT START WITH 1;
CREATE TABLE IF NOT EXISTS restaurant
(
    id   BIGINT DEFAULT NEXT VALUE FOR restaurant_id_seq NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Menu
CREATE SEQUENCE IF NOT EXISTS menu_id_seq AS BIGINT START WITH 1;
CREATE TABLE IF NOT EXISTS menu (
    id            BIGINT DEFAULT NEXT VALUE FOR menu_id_seq NOT NULL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    date          DATE NOT NULL,
    dishes        VARCHAR(1000) NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_menu_on_restaurant_id ON menu (restaurant_id);
CREATE UNIQUE INDEX IF NOT EXISTS index_menu_on_restaurant_id_and_date ON menu (date, restaurant_id);