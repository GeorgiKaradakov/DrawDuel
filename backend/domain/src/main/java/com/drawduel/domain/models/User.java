package com.drawduel.domain.models;

import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@AllArgsConstructor
public class User {
  private final UUID id;
  private final String username;
  private final String email;
  private final String passHash;
  private final Instant createdAt;

  public User(String username, String email, String passHash, Instant createdAt) {
    this.id = UUID.randomUUID();
    this.username = username;
    this.email = email;
    this.passHash = passHash;
    this.createdAt = createdAt;
  }
}
