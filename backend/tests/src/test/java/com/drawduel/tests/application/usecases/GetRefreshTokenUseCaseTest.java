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

class GetRefreshTokenUseCaseTest {

  private RefreshTokenRepository tokenRepo;
  private UserSessionDomainToDtoMapper mapper;
  private GetRefreshTokenUseCase useCase;

  @BeforeEach
  void setup() {
    tokenRepo = mock(RefreshTokenRepository.class);
    mapper = mock(UserSessionDomainToDtoMapper.class);
    useCase = new GetRefreshTokenUseCase(tokenRepo, mapper);
  }

  @Test
  void returnsSessionDto() {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "127.0.0.1",
            "Firefox",
            "Earth",
            "Linux",
            "refresh",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    when(tokenRepo.findByRefreshToken("refresh")).thenReturn(Optional.of(session));

    when(mapper.toDto(session))
        .thenReturn(
            new UserSessionDto(
                session.getId(),
                session.getUserId(),
                session.getSessionId(),
                session.getIpAddress(),
                session.getUserAgent(),
                session.getLocation(),
                session.getOsName(),
                session.getRefreshToken(),
                session.getRevoked(),
                session.getIssuedAt(),
                session.getExpiresAt()));

    var result = useCase.handle(new GetRefreshTokenUseCasePort.Query("refresh"));

    assertThat(result.response()).isNotNull();
  }
}
