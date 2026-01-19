ALTER TABLE tokens
ADD COLUMN session_id UUID NOT NULL;

ALTER TABLE tokens
ADD COLUMN os_name VARCHAR(100);

CREATE INDEX IF NOT EXISTS idx_tokens_session_id
ON tokens (session_id);

CREATE INDEX IF NOT EXISTS idx_tokens_user_id
ON tokens (user_id);
