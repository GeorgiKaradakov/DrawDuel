package com.drawduel.application.dtos;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserSessionDto {
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
