CREATE TABLE IF NOT EXISTS callsign
(
    id SERIAL,
    tg_id BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    user_name VARCHAR(100),
    k2_call_sign VARCHAR(100) NOT NULL UNIQUE,
    official_call_sign VARCHAR(100) UNIQUE,
    qth VARCHAR(100),
    PRIMARY KEY(id)
);
