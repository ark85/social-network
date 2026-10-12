CREATE TABLE IF NOT EXISTS public.posts
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    text text,
    creation_date_time timestamp,
    CONSTRAINT posts_pkey PRIMARY KEY (id)
)
