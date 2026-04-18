CREATE TABLE user_profiles
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL UNIQUE,
    full_name    VARCHAR(255),
    email        VARCHAR(255),
    phone_number VARCHAR(50),
    address      TEXT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
