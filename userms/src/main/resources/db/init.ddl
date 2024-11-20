DROP SCHEMA IF EXISTS app CASCADE;

CREATE SCHEMA app;

CREATE TABLE app.users
(
    id          UUID,
    author_username    VARCHAR(100),
    email       VARCHAR(50),
    role        VARCHAR(10),
    active      BOOLEAN,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    password    TEXT,
    register_at DATE,
    updated_at  DATE,
    CONSTRAINT pr_id PRIMARY KEY (id),
    CONSTRAINT username_uniq UNIQUE (author_username),
    CONSTRAINT email UNIQUE (email)
)
