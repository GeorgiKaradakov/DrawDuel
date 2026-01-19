package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.RevokeSessionUseCasePort;
import com.drawduel.application.usecases.RevokeSessionUseCase;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RevokeSessionUseCaseTest {

  private RefreshTokenRepository refreshTokenRepository;
  private RevokeSessionUseCasePort useCase;

  @BeforeEach
  void setup() {
    refreshTokenRepository = mock(RefreshTokenRepository.class);
    useCase = new RevokeSessionUseCase(refreshTokenRepository);
  }

  @Test
  void shouldRevokeOtherSession() {
    UUID userId = UUID.randomUUID();
    UUID sessionId = UUID.randomUUID();
    UUID currentSessionId = UUID.randomUUID();

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            sessionId,
            "127.0.0.1",
            "Chrome",
            "Earth",
            "Linux",
            "refresh",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.findActiveSessionBySessionId(sessionId))
        .thenReturn(Optional.of(session));

    useCase.handle(new RevokeSessionUseCasePort.Query(userId, sessionId, currentSessionId));

    verify(refreshTokenRepository).revokeRefreshToken("refresh");
  }

  @Test
  void shouldNotAllowRevokingCurrentSession() {
    UUID userId = UUID.randomUUID();
    UUID sessionId = UUID.randomUUID();

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            sessionId,
            "127.0.0.1",
            "Chrome",
            "Earth",
            "Linux",
            "refresh",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.findActiveSessionBySessionId(sessionId))
        .thenReturn(Optional.of(session));

    assertThatThrownBy(
            () -> useCase.handle(new RevokeSessionUseCasePort.Query(userId, sessionId, sessionId)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot revoke current session");
  }
}
