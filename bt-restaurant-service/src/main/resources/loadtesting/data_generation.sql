INSERT INTO cuisines(name, description)
SELECT
    'Cuisine ' || i,
    'Description ' || i
FROM generate_series(1, 100000) i;

INSERT INTO restaurants(title, description, address, avg_sum, menu, is_published)
SELECT
    'Restaurant ' || i,
    'Description ' || i,
    'Address ' || i,
    (random() * 5000)::numeric,
    'menu_' || i || '.pdf',
    (random() > 0.5)
FROM generate_series(1, 100000) i;

INSERT INTO restaurants_cuisines(restaurant_id, cuisine_id)
SELECT r.id, c.id
FROM restaurants r
         CROSS JOIN LATERAL (
    SELECT id FROM cuisines ORDER BY random() LIMIT 3
    ) c
ON CONFLICT (restaurant_id, cuisine_id) DO NOTHING;

INSERT INTO tables(restaurant_id, seats, description, is_available)
SELECT
    r.id,
    (random()*6 + 2)::int,
    'Table ' || g,
    random() > 0.2
FROM restaurants r
         CROSS JOIN generate_series(1,10) g;
