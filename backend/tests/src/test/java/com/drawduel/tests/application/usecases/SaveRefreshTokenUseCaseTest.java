package com.drawduel.tests.application.usecases;

import static org.mockito.Mockito.*;

import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.application.usecases.SaveRefreshTokenUseCase;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaveRefreshTokenUseCaseTest {
  private RefreshTokenRepository tokenRepo;
  private SaveRefreshTokenUseCasePort useCase;

  @BeforeEach
  void setup() {
    tokenRepo = mock(RefreshTokenRepository.class);
    useCase = new SaveRefreshTokenUseCase(tokenRepo);
  }

  @Test
  void shouldSaveTokenToRepo() {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "127.0.0.1",
            "Mozilla/5.0",
            "Earth",
            "refresh",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    useCase.handle(new SaveRefreshTokenUseCase.Query(session));

    verify(tokenRepo, times(1)).save(session);
  }
}
