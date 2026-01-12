package com.drawduel.application.usecases;

import com.drawduel.application.ports.LogoutUseCasePort;
import com.drawduel.domain.ports.RefreshTokenRepository;

public class LogoutUseCase implements LogoutUseCasePort {

  private final RefreshTokenRepository refreshTokenRepository;

  public LogoutUseCase(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void handle(Query query) {
    String refreshToken = query.refreshToken();

    if (refreshToken == null || refreshToken.isBlank()) {
      throw new IllegalArgumentException("No refresh token provided");
    }

    refreshTokenRepository.revokeRefreshToken(refreshToken);
  }
}
