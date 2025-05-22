CREATE TABLE users (
                       id            BIGSERIAL PRIMARY KEY,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       username      VARCHAR(50)  NOT NULL UNIQUE,
                       password      VARCHAR(255) NOT NULL,
                       role          VARCHAR(255) NOT NULL,
                       created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE OR REPLACE FUNCTION set_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at_column();