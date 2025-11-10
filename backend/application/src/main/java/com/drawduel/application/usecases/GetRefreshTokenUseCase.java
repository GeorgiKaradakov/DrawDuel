package com.drawduel.application.usecases;

import com.drawduel.application.mapper.UserSessionDomainToDtoMapper;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetRefreshTokenUseCase implements GetRefreshTokenUseCasePort {
  private final RefreshTokenRepository tokenRepo;
  private final UserSessionDomainToDtoMapper mapper;

  @Override
  public Result handle(Query q) {
    String refreshToken = q.refreshToken();
    UserSession tokenEntity =
        tokenRepo
            .findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

    return new Result(mapper.toDto(tokenEntity));
  }
}
