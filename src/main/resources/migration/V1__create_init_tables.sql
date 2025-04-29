CREATE TABLE IF NOT EXISTS public.authors (
    id          SERIAL      PRIMARY KEY,
    biography   TEXT,
    birth_date  DATE,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS public.books (
    id               SERIAL       PRIMARY KEY,
    author_id        INTEGER      NOT NULL
    REFERENCES public.authors(id)
    ON DELETE CASCADE,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    genre            VARCHAR(100),
    isbn             VARCHAR(20)  UNIQUE,
    price            NUMERIC(10,2),
    publication_date DATE
    );

CREATE TABLE IF NOT EXISTS public.orders (
    id             BIGSERIAL PRIMARY KEY,
    book_id        BIGINT     NOT NULL
    REFERENCES public.books(id)
    ON DELETE CASCADE,
    customer_name  VARCHAR(255) NOT NULL,
    status         VARCHAR(50),
    order_date     DATE
    );
