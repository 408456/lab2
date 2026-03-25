CREATE TABLE IF NOT EXISTS cuisines
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(100),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
    );

COMMENT ON TABLE cuisines IS 'Таблица типов кухонь';
COMMENT ON COLUMN cuisines.id IS 'Уникальный идентификатор типа кухни';
COMMENT ON COLUMN cuisines.name IS 'Название кухни';
COMMENT ON COLUMN cuisines.description IS 'Описание кухни';
COMMENT ON COLUMN cuisines.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN cuisines.updated_at IS 'Дата и время последнего обновления записи';

CREATE TABLE IF NOT EXISTS restaurants
(
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255)   NOT NULL,
    description   TEXT,
    address       VARCHAR(255)   NOT NULL,
    avg_sum       NUMERIC        DEFAULT 0,
    menu          VARCHAR(255),
    is_published  BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP      NOT NULL DEFAULT NOW()
    );

COMMENT ON TABLE restaurants IS 'Таблица ресторанов';
COMMENT ON COLUMN restaurants.id IS 'Уникальный идентификатор ресторана';
COMMENT ON COLUMN restaurants.title IS 'Название ресторана';
COMMENT ON COLUMN restaurants.description IS 'Описание ресторана';
COMMENT ON COLUMN restaurants.address IS 'Адрес ресторана';
COMMENT ON COLUMN restaurants.avg_sum IS 'Средняя оценка ресторана';
COMMENT ON COLUMN restaurants.menu IS 'Ссылка на файл меню в формате PDF';
COMMENT ON COLUMN restaurants.is_published IS 'Флаг публикации ресторана';
COMMENT ON COLUMN restaurants.created_at IS 'Дата и время создания ресторана';
COMMENT ON COLUMN restaurants.updated_at IS 'Дата и время последнего обновления ресторана';

CREATE TABLE IF NOT EXISTS restaurants_cuisines
(
    restaurant_id BIGINT NOT NULL,
    cuisine_id    BIGINT NOT NULL,
    PRIMARY KEY (restaurant_id, cuisine_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    FOREIGN KEY (cuisine_id)    REFERENCES cuisines (id)    ON DELETE CASCADE
    );

COMMENT ON TABLE restaurants_cuisines IS 'Таблица связи ресторанов с кухонями';
COMMENT ON COLUMN restaurants_cuisines.restaurant_id IS 'Идентификатор ресторана';
COMMENT ON COLUMN restaurants_cuisines.cuisine_id IS 'Идентификатор типа кухни';

CREATE INDEX idx_restaurants_cuisines_restaurant_id ON restaurants_cuisines(restaurant_id);
CREATE INDEX idx_restaurants_cuisines_cuisine_id ON restaurants_cuisines(cuisine_id);

CREATE TABLE IF NOT EXISTS tables
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT      NOT NULL,
    seats         INTEGER     NOT NULL CHECK (seats > 0),
    description   VARCHAR(255),
    is_available  BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tables_restaurant FOREIGN KEY (restaurant_id)
    REFERENCES restaurants (id) ON DELETE CASCADE
    );

COMMENT ON TABLE tables IS 'Столы в ресторанах';
COMMENT ON COLUMN tables.id IS 'Уникальный идентификатор стола';
COMMENT ON COLUMN tables.restaurant_id IS 'Идентификатор ресторана, которому принадлежит стол';
COMMENT ON COLUMN tables.seats IS 'Количество посадочных мест за столом';
COMMENT ON COLUMN tables.description IS 'Описание стола';
COMMENT ON COLUMN tables.is_available IS 'Доступен ли стол для бронирования';
COMMENT ON COLUMN tables.created_at IS 'Дата и время добавления стола';