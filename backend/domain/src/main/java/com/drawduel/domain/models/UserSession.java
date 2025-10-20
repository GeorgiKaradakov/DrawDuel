package com.drawduel.domain.models;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSession {

  private UUID id;
  private UUID userId;
  private String ipAddress;
  private String userAgent;
  private String location;
  private String refreshToken;
  private Boolean revoked;
  private Instant issuedAt;
  private Instant expiresAt;
}
