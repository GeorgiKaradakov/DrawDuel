package com.drawduel.domain.models;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
  private UUID id;
  private String username;
  private String email;
  private String passHash;
  private Instant createdAt;
  private String profileImageUrl;
}
