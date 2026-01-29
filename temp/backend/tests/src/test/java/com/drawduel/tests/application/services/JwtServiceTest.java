package com.drawduel.tests.application.services;

import static org.assertj.core.api.Assertions.*;

import com.drawduel.application.services.JwtService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private JwtService jwtService;
  private static final String SECRET =
      "a_secure_secret_key_that_is_long_enough_for_hmac_sha256_123456789";
  private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour

  private UUID userId;
  private String username;
  private String email;

  @BeforeEach
  void setup() {
    jwtService = new JwtService(SECRET, EXPIRATION_MS);
    userId = UUID.randomUUID();
    username = "guts";
    email = "guts@drawduel.com";
  }

  @Test
  void shouldGenerateValidAccessToken() {
    UUID sessionId = UUID.randomUUID();
    String token = jwtService.generateToken(userId, sessionId);

    assertThat(token).isNotNull();
    assertThat(jwtService.isTokenValid(token)).isTrue();

    assertThat(jwtService.extractUserId(token)).isEqualTo(userId);
    assertThat(jwtService.extractSessionIdAllowExpired(token)).isEqualTo(sessionId);
  }

  @Test
  void shouldDetectInvalidToken() {
    String invalidToken = "abc.def.ghi";
    assertThat(jwtService.isTokenValid(invalidToken)).isFalse();
  }

  @Test
  void shouldGenerateUniqueRefreshTokens() {
    String token1 = jwtService.generateRefreshToken();
    String token2 = jwtService.generateRefreshToken();

    assertThat(token1).isNotEqualTo(token2);
    assertThat(token1).doesNotContain("=");
    assertThat(token1).doesNotContain("+");
  }

  @Test
  void shouldFailValidationForDifferentSecret() {
    String token = jwtService.generateToken(userId, UUID.randomUUID());
    JwtService differentKeyService =
        new JwtService("another_secret_key_987654321_but_it_needs_more_bytes", EXPIRATION_MS);

    assertThat(differentKeyService.isTokenValid(token)).isFalse();
  }
}
