package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.LogoutUseCasePort;
import com.drawduel.application.usecases.LogoutUseCase;
import com.drawduel.domain.ports.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoutUseCaseTest {

  private RefreshTokenRepository refreshTokenRepository;
  private LogoutUseCasePort useCase;

  @BeforeEach
  void setup() {
    refreshTokenRepository = mock(RefreshTokenRepository.class);
    useCase = new LogoutUseCase(refreshTokenRepository);
  }

  @Test
  void shouldRevokeRefreshToken() {
    useCase.handle(new LogoutUseCasePort.Query("refresh-token"));

    verify(refreshTokenRepository).revokeRefreshToken("refresh-token");
  }

  @Test
  void shouldThrowIfTokenMissing() {
    assertThatThrownBy(() -> useCase.handle(new LogoutUseCasePort.Query(null)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
