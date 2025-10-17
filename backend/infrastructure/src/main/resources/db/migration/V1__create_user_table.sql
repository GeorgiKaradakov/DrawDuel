create table users (
  created_at timestamp(6) with time zone not null,
  id uuid not null,
  username varchar(50) not null unique,
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  primary key (id)
);
