ALTER TABLE games
  RENAME COLUMN player1id TO playerAid;

ALTER TABLE games
  RENAME COLUMN player2id TO playerBid;

ALTER TABLE games
  RENAME COLUMN player1draw_points TO playerAdraw_points;

ALTER TABLE games
  RENAME COLUMN player1guess_points TO playerAguess_points;

ALTER TABLE games
  RENAME COLUMN player2draw_points TO playerBdraw_points;

ALTER TABLE games
  RENAME COLUMN player2guess_points TO playerBguess_points;

-- 2️⃣ Add the new columns
ALTER TABLE games
  ADD COLUMN total_rounds INTEGER NOT NULL DEFAULT 0,
  ADD COLUMN current_rounds INTEGER NOT NULL DEFAULT 0;

-- 3️⃣ Update the status column to enforce valid values
ALTER TABLE games
  ADD CONSTRAINT games_status_check CHECK (status IN ('WAITING', 'IN_PROGRESS', 'FINISHED'));
