CREATE TABLE a_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(6) NOT NULL,
    user_id BIGINT NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES public.users(id)
);
