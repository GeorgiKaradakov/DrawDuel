package com.drawduel.application.usecases;

import com.drawduel.application.ports.RevokeRefreshTokenUseCasePort;
import com.drawduel.domain.ports.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RevokeRefreshTokenUseCase implements RevokeRefreshTokenUseCasePort {
  private final RefreshTokenRepository refreshTokenRepo;

  @Override
  public void handle(Query c) {
    String token = c.refreshToken();
    refreshTokenRepo.revokeRefreshToken(token);
  }
}
