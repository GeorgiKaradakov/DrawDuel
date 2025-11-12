package com.drawduel.tests.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.application.services.JwtService;
import com.drawduel.application.services.TokensService;
import com.drawduel.domain.models.User;
import com.drawduel.domain.models.UserSession;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokensServiceTest {

  private JwtService jwtService;
  private SaveRefreshTokenUseCasePort saveRefreshTokenUseCase;
  private TokensService tokensService;

  private static final String SECRET = "super_secure_secret_key_12345678910_for_tests";
  private static final long EXPIRATION_MS = 1000 * 60 * 60;
  private static final int REFRESH_VALIDITY_DAYS = 7;

  @BeforeEach
  void setup() {
    jwtService = spy(new JwtService(SECRET, EXPIRATION_MS));
    saveRefreshTokenUseCase = mock(SaveRefreshTokenUseCasePort.class);
    tokensService = new TokensService(jwtService, saveRefreshTokenUseCase, REFRESH_VALIDITY_DAYS);
  }

  @Test
  void shouldGenerateAccessTokenSuccessfully() {
    UUID userId = UUID.randomUUID();
    String accessToken = tokensService.generateAccessToken(userId, "guts", "guts@drawduel.com");

    assertThat(accessToken).isNotNull();
    assertThat(jwtService.isTokenValid(accessToken)).isTrue();
  }

  @Test
  void shouldGenerateTokensAndSaveRefreshTokenForUser() {
    User user = new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hash", Instant.now());

    String[] tokens = tokensService.generateTokens(user, "127.0.0.1", "Mozilla/5.0", "Earth");

    assertThat(tokens[0]).isNotNull(); // Access token
    assertThat(tokens[1]).isNotNull(); // Refresh token
    verify(saveRefreshTokenUseCase, times(1)).handle(any(SaveRefreshTokenUseCasePort.Query.class));
  }

  @Test
  void shouldGenerateTokensAndSaveRefreshTokenForUserDto() {
    UserDto userDto = new UserDto(UUID.randomUUID(), "guts", "guts@drawduel.com", Instant.now());

    String[] tokens = tokensService.generateTokens(userDto, "127.0.0.1", "Chrome", "Earth");

    assertThat(tokens[0]).isNotNull();
    assertThat(tokens[1]).isNotNull();
    verify(saveRefreshTokenUseCase, times(1)).handle(any(SaveRefreshTokenUseCasePort.Query.class));
  }

  @Test
  void shouldCreateUserSessionWithCorrectExpiry() {
    UUID userId = UUID.randomUUID();

    tokensService.generateTokens(
        new User(userId, "guts", "guts@drawduel.com", "hash", Instant.now()),
        "127.0.0.1",
        "Chrome",
        "Earth");

    // Capture argument to check session data
    var captor = org.mockito.ArgumentCaptor.forClass(SaveRefreshTokenUseCasePort.Query.class);
    verify(saveRefreshTokenUseCase).handle(captor.capture());

    UserSession session = captor.getValue().session();
    assertThat(session.getExpiresAt()).isAfter(Instant.now());
    assertThat(session.getIpAddress()).isEqualTo("127.0.0.1");
    assertThat(session.getUserAgent()).isEqualTo("Chrome");
    assertThat(session.getLocation()).isEqualTo("Earth");
    assertThat(session.getRevoked()).isFalse();
  }
}
