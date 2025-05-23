alter table public.orders
    add column user_id BIGINT not null default 0;