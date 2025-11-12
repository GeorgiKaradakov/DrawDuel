package com.drawduel.tests.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.domain.models.UserSession;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UserSessionTest {
  @Test
  void shouldCreateUserSessionWithGivenValues() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(3600);
    UserSession session =
        new UserSession(
            id,
            userId,
            "127.0.0.1",
            "Mozilla/5.0",
            "Earth",
            "refreshToken123",
            false,
            issuedAt,
            expiresAt);

    assertThat(session.getId()).isEqualTo(id);
    assertThat(session.getUserId()).isEqualTo(userId);
    assertThat(session.getIpAddress()).isEqualTo("127.0.0.1");
    assertThat(session.getUserAgent()).isEqualTo("Mozilla/5.0");
    assertThat(session.getLocation()).isEqualTo("Earth");
    assertThat(session.getRefreshToken()).isEqualTo("refreshToken123");
    assertThat(session.getRevoked()).isFalse();
    assertThat(session.getIssuedAt()).isEqualTo(issuedAt);
    assertThat(session.getExpiresAt()).isEqualTo(expiresAt);
  }
}
