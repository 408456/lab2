INSERT INTO users (id, keycloak_id, first_name, last_name, phone, email, is_verified, role)
VALUES (1,
        '8cdb789a-bb65-42e5-b4f5-cd64dc5cfa63'::uuid,
        'Админ',
        'Админов',
        '+79000000001',
        'admin@gmail.com',
        TRUE,
        'ADMIN'),

       (2,
        '28a4e187-12a0-4fd6-9bd7-a154b4b2f449'::uuid,
        'Клиент',
        'Клиентов',
        '+79000000002',
        'client@gmail.com',
        TRUE,
        'CLIENT');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));