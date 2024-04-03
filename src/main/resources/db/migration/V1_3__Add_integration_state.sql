CREATE TABLE IF NOT EXISTS integration
(
    id SERIAL,
    PRIMARY KEY(id),
    callsign_id SERIAL REFERENCES callsign(id)
    );