package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.GetSessionInfoUseCasePort;
import com.drawduel.application.usecases.GetSessionInfoUseCase;
import com.drawduel.domain.enums.DeviceStatus;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetSessionInfoUseCaseTest {

  private RefreshTokenRepository refreshTokenRepository;
  private GetSessionInfoUseCasePort useCase;

  @BeforeEach
  void setup() {
    refreshTokenRepository = mock(RefreshTokenRepository.class);
    useCase = new GetSessionInfoUseCase(refreshTokenRepository);
  }

  @Test
  void shouldMarkCurrentSessionCorrectly() {
    UUID userId = UUID.randomUUID();
    UUID currentSessionId = UUID.randomUUID();

    UserSession current =
        new UserSession(
            UUID.randomUUID(),
            userId,
            currentSessionId,
            "127.0.0.1",
            "Firefox",
            "Earth",
            "Linux",
            "token1",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    UserSession other =
        new UserSession(
            UUID.randomUUID(),
            userId,
            UUID.randomUUID(),
            "127.0.0.2",
            "Chrome",
            "Mars",
            "Windows",
            "token2",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.findActiveSessionsByUserId(userId))
        .thenReturn(List.of(current, other));

    var result = useCase.handle(new GetSessionInfoUseCasePort.Query(userId, currentSessionId));

    assertThat(result.devices().getDevices()).hasSize(2);
    assertThat(result.devices().getDevices())
        .anyMatch(d -> d.getStatus() == DeviceStatus.CURRENT_SESSION)
        .anyMatch(d -> d.getStatus() == DeviceStatus.ACTIVE);
  }
}
