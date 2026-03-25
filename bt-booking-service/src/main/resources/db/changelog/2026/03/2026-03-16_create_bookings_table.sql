DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'booking_status') THEN
CREATE TYPE booking_status AS ENUM ('CREATED', 'CONFIRMED', 'CANCELLED', 'COMPLETED');
END IF;
END$$;

CREATE TABLE IF NOT EXISTS bookings
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT         NOT NULL,
    user_id       BIGINT         NOT NULL,
    table_id      BIGINT         NOT NULL,
    start_time    TIMESTAMP      NOT NULL,
    end_time      TIMESTAMP      NOT NULL,
    guests_count  INTEGER        NOT NULL CHECK (guests_count > 0),
    status        VARCHAR(20)    NOT NULL DEFAULT 'CREATED',
    created_at    TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT check_booking_interval
    CHECK (end_time > start_time AND (end_time - start_time) <= INTERVAL '3 hours')
);

COMMENT ON TABLE bookings IS 'Бронирования столов';
COMMENT ON COLUMN bookings.id IS 'Уникальный идентификатор брони';
COMMENT ON COLUMN bookings.restaurant_id IS 'Идентификатор ресторана';
COMMENT ON COLUMN bookings.user_id IS 'Идентификатор пользователя, создавшего бронь';
COMMENT ON COLUMN bookings.table_id IS 'Идентификатор забронированного стола';
COMMENT ON COLUMN bookings.start_time IS 'Дата и время начала бронирования';
COMMENT ON COLUMN bookings.end_time IS 'Дата и время окончания бронирования (не более 3 часов от начала)';
COMMENT ON COLUMN bookings.guests_count IS 'Количество гостей';
COMMENT ON COLUMN bookings.status IS 'Статус брони (CREATED, CONFIRMED, CANCELLED, COMPLETED)';
COMMENT ON COLUMN bookings.created_at IS 'Дата и время создания брони';