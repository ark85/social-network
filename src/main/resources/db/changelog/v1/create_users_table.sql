CREATE TABLE IF NOT EXISTS public.users
(
    id text NOT NULL,
    first_name text NOT NULL,
    second_name text NOT NULL,
    password_hash text NOT NULL,
    birth_date text,
    biography text,
    city text,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)
