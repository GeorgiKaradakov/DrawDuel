package com.drawduel.application.dtos;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class UserSessionDto {
  private UUID id;
  private UUID userId;
  private UUID sessionId;
  private String ipAddress;
  private String userAgent;
  private String location;
  private String osName;
  private String refreshToken;
  private Boolean revoked;
  private Instant issuedAt;
  private Instant expiresAt;
}
