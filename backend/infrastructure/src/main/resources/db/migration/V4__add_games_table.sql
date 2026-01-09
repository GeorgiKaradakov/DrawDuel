create table games (
  id uuid not null,
  player1id uuid not null,
  player2id uuid not null,
  winner_id uuid,

  player1draw_points integer not null,
  player1guess_points integer not null,
  player2draw_points integer not null,
  player2guess_points integer not null,

  status varchar(255) not null,

  ended_at timestamp(6) with time zone,
  started_at timestamp(6) with time zone not null,

  primary key (id)
);
