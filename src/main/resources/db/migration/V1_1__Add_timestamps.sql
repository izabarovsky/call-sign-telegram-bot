ALTER TABLE callsign
    ADD COLUMN creation_timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN modification_timestamp TIMESTAMPTZ NOT NULL DEFAULT now();