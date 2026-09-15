CREATE TABLE IF NOT EXISTS public.dialog_messages
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    from_id uuid NOT NULL,
    to_id uuid NOT NULL,
    text text,
    creation_date_time timestamp,
    CONSTRAINT dialog_messages_pkey PRIMARY KEY (id),
    CONSTRAINT dialog_messages_from_user_fkey FOREIGN KEY (from_id) REFERENCES users(id),
    CONSTRAINT dialog_messages_to_user_fkey FOREIGN KEY (to_id) REFERENCES users(id)
)
