create table tokens (revoked boolean not null,
  expires_at timestamp(6) with time zone not null,
  issued_at timestamp(6) with time zone not null,
  id uuid not null,
  user_id uuid not null,
  refresh_token varchar(512) not null unique,
  ip_adress varchar(255),
  location varchar(255),
  user_agent varchar(255),
  primary key (id)
);
