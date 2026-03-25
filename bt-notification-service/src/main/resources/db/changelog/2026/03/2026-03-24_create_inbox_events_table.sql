CREATE TABLE inbox_events
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(100),
    aggregate_id   BIGINT,
    type           VARCHAR(100) NOT NULL,
    payload        TEXT         NOT NULL,
    received_at    TIMESTAMP    NOT NULL,
    processed      BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_inbox_processed ON inbox_events (processed);

CREATE INDEX idx_inbox_type ON inbox_events (type);

CREATE INDEX idx_inbox_aggregate ON inbox_events (aggregate_type, aggregate_id);
