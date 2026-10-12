CREATE TABLE IF NOT EXISTS public.friends
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    friend_id UUID NOT NULL,

    CONSTRAINT friends_pkey PRIMARY KEY (id),
    CONSTRAINT fk_friends_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_friends_friend FOREIGN KEY (friend_id) REFERENCES users(id),
    CONSTRAINT uk_friends UNIQUE (user_id, friend_id),
    CONSTRAINT chk_friends_not_self CHECK (user_id <> friend_id)
)
