CREATE TABLE tt_users.user(
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    password_hash INTEGER NOT NULL,
    is_deleted BOOLEAN DEFAULT false,
    deleted_at TIMESTAMP DEFAULT NULL
)