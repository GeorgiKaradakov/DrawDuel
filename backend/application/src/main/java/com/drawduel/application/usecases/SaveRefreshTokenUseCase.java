package com.drawduel.application.usecases;

import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.domain.ports.RefreshTokenRepository;

public class SaveRefreshTokenUseCase implements SaveRefreshTokenUseCasePort {
  private final RefreshTokenRepository refreshTokenRepository;

  public SaveRefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void handle(Query q) {
    System.out.println(q.session().getSessionId());
    refreshTokenRepository.save(q.session());
  }
}
