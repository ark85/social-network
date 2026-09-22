CREATE TABLE IF NOT EXISTS public.dialog_messages
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    dialog_id uuid NOT NULL,
    from_user_id uuid NOT NULL,
    to_user_id uuid NOT NULL,
    text text,
    creation_date_time timestamp,
    CONSTRAINT dialog_messages_pkey PRIMARY KEY (dialog_id, id)
)
