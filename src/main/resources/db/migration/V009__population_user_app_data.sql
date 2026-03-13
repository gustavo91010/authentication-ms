INSERT INTO applications (
    register_url,
    name,
    client_id,
    secret_key,
    created_at
)
VALUES (
    'http://localhost:8082',
    'authentication_ms',
    'authentication_ms_client',
    '77a1e032-823a-4793-ac96-2e34567b3059',
    now()
);

INSERT INTO user_app_data (
    user_id,
    app_id,
    password,
    created_at,
    is_active,
    access_token
)
SELECT
    u.id,
    a.id,
    u.password,
    u.created_at,
    u.active,
    u.access_token::uuid
FROM users u
JOIN applications a 
    ON a.name = u.aplication;
