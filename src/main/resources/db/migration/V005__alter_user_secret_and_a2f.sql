BEGIN;

ALTER TABLE public.users
ADD COLUMN secret character varying(100);

ALTER TABLE public.users
ADD COLUMN a2fActive  boolean DEFAULT false;

COMMIT;
