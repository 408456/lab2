INSERT INTO bookings (restaurant_id, user_id, table_id, start_time, end_time, guests_count, status)
VALUES (1, 2, 1,
        '2026-10-15 18:00:00', '2026-10-15 20:00:00', 2, 'CREATED'),
       (1, 1, 3,
        '2026-05-11 18:00:00', '2026-05-11 20:30:00', 5, 'CONFIRMED'),
       (2, 1, 1,
        '2026-05-15 18:00:00', '2026-05-15 19:00:00', 2, 'COMPLETED'),
       (2, 2, 5,
        '2026-11-10 19:00:00', '2026-11-10 21:00:00', 2, 'CANCELLED');