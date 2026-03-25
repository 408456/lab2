INSERT INTO cuisines (name, description)
VALUES ('Итальянская кухня',
        'Традиционные блюда Италии'),
       ('Русская кухня',
        'Русская традиционная кухня'),
       ('Японская кухня',
        'Японская кухня')
;

INSERT INTO restaurants (title, description, address, avg_sum, menu, is_published)
VALUES ('Chang',
        'Ресторан японской кухни',
        'Москва, ул. Тверская, 12',
        1000,
        'https://example.com/menu/la-la.pdf',
        TRUE),
       ('Terrassa',
        'Смешанная кухня',
        'Москва, ул. Арбат, 8',
        20000,
        'https://example.com/menu/terrassa.pdf',
        TRUE);

INSERT INTO restaurants_cuisines (restaurant_id, cuisine_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (2, 3);

INSERT INTO tables (restaurant_id, seats, description, is_available)
VALUES (1, 2, 'Маленький столик у окна', TRUE),
       (1, 4, 'Стол в центре зала', FALSE),
       (1, 6, 'Большой стол для компании', TRUE),
       (1, 8, 'Банкетный стол', FALSE),

       (2, 2, 'Столик в углу для двоих', TRUE),
       (2, 4, 'Круглый стол для компании', FALSE),
       (2, 6, 'Семейный стол', TRUE),
       (2, 8, 'Стол на террасе', FALSE);
