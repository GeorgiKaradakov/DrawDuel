package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.UserSessionDto;
import com.drawduel.application.mapper.UserSessionDomainToDtoMapper;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.application.usecases.GetRefreshTokenUseCase;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetRefreshTokenUseCaseTest {

  private GetRefreshTokenUseCasePort getRefreshTokenUseCase;
  private RefreshTokenRepository tokenRepo;
  private UserSessionDomainToDtoMapper mapper;

  @BeforeEach
  void setup() {
    tokenRepo = mock(RefreshTokenRepository.class);
    mapper = mock(UserSessionDomainToDtoMapper.class);
    getRefreshTokenUseCase = new GetRefreshTokenUseCase(tokenRepo, mapper);
  }

  @Test
  void shouldReturnUserSessionDtoWhenFound() {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "127.0.0.1",
            "Mozzila/5.0",
            "Earth",
            "refreshtoken123",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));
    when(tokenRepo.findByRefreshToken("refreshtoken123")).thenReturn(Optional.of(session));
    when(mapper.toDto(session))
        .thenReturn(
            new UserSessionDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "127.0.0.1",
                "Mozzila/5.0",
                "Earth",
                "refreshtoken123",
                false,
                Instant.now(),
                Instant.now().plusSeconds(3600)));

    var result =
        getRefreshTokenUseCase.handle(new GetRefreshTokenUseCasePort.Query("refreshtoken123"));

    assertThat(result.response()).isNotNull();
    verify(tokenRepo).findByRefreshToken("refreshtoken123");
  }
}
