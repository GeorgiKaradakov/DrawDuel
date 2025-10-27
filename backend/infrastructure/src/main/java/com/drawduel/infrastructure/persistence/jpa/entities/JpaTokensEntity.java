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
@Table(name = "tokens")
public class JpaTokensEntity {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID Id;

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

  @Column(nullable = false, updatable = false)
  private Instant issuedAt = Instant.now();
}
