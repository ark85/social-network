CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    first_name text NOT NULL,
    second_name text NOT NULL,
    password_hash text NOT NULL,
    birth_date DATE,
    biography text,
    city text,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)
