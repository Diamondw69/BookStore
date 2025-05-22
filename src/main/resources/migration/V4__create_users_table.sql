CREATE TABLE users (
                       id            BIGSERIAL PRIMARY KEY,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       username      VARCHAR(50)  NOT NULL UNIQUE,
                       password      VARCHAR(255) NOT NULL,
                       role          VARCHAR(255) NOT NULL,
                       created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);