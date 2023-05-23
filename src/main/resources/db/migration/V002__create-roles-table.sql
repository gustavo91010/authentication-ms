BEGIN;

CREATE TABLE IF NOT EXISTS public.roles
(
    id bigserial NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.users_roles
(
    users_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (users_id, roles_id)
);

END;