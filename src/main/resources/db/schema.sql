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
    dishes        JSON NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_menu_on_date ON menu (date);
CREATE UNIQUE INDEX IF NOT EXISTS index_menu_on_restaurant_id_and_date ON menu (restaurant_id, date);

-- Vote
CREATE SEQUENCE IF NOT EXISTS vote_id_seq AS BIGINT START WITH 1;
CREATE TABLE IF NOT EXISTS vote (
    id            BIGINT DEFAULT NEXT VALUE FOR vote_id_seq NOT NULL PRIMARY KEY,
    meal_date      DATE NOT NULL,
    user_id       BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES menu ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_vote_on_user_id_and_meal_date ON vote (user_id, meal_date);
CREATE INDEX IF NOT EXISTS index_vote_on_meal_date ON vote (meal_date);