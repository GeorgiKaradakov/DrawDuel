package com.drawduel.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "tokens",
    indexes = {
      @Index(name = "idx_tokens_session_id", columnList = "sessionId"),
      @Index(name = "idx_tokens_user_id", columnList = "user_id")
    })
public class JpaTokensEntity {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID Id;

  @Column(nullable = false, columnDefinition = "uuid")
  private UUID sessionId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private JpaUserEntity user;

  @Column(nullable = false, unique = true, length = 512)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private Boolean revoked;

  private String ipAdress;
  private String userAgent;
  private String location;
  private String osName;

  @Column(nullable = false, updatable = false)
  private Instant issuedAt = Instant.now();
}
