CREATE TABLE outbox_event
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(50),
    aggregate_id   BIGINT,
    type           VARCHAR(50),
    payload        text,
    created_at     TIMESTAMP,
    processed      BOOLEAN DEFAULT FALSE
);
