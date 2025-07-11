BEGIN;

ALTER TABLE public.users
ADD COLUMN name character varying(100);

END;
