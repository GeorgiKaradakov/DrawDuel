package com.drawduel.domain.models;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User {
  private final UUID id;
  private final String username;
  private final String email;
  private final String passHash;
  private final Instant createdAt;

  public User(UUID id, String username, String email, String passHash, Instant createdAt) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.passHash = passHash;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassHash() {
    return passHash;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
