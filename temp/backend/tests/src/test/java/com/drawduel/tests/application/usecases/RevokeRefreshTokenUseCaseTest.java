package com.drawduel.tests.application.usecases;

import static org.mockito.Mockito.*;

import com.drawduel.application.ports.RevokeRefreshTokenUseCasePort;
import com.drawduel.application.usecases.RevokeRefreshTokenUseCase;
import com.drawduel.domain.ports.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RevokeRefreshTokenUseCaseTest {
  private RefreshTokenRepository tokenRepo;
  private RevokeRefreshTokenUseCasePort useCase;

  @BeforeEach
  void setup() {
    tokenRepo = mock(RefreshTokenRepository.class);
    useCase = new RevokeRefreshTokenUseCase(tokenRepo);
  }

  @Test
  void shouldCallRepoToRevokeToken() {
    useCase.handle(new RevokeRefreshTokenUseCase.Query("refresh123"));
    verify(tokenRepo, times(1)).revokeRefreshToken("refresh123");
  }
}
