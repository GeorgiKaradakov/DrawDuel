package com.drawduel.application.usecases;

import com.drawduel.application.ports.RevokeSessionUseCasePort;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RevokeSessionUseCase implements RevokeSessionUseCasePort {

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void handle(Query query) {
    UUID userId = query.userId();
    UUID sessionId = query.sessionId();
    UUID currrentSessionId = query.currentSessionId();

    UserSession session =
        refreshTokenRepository
            .findActiveSessionBySessionId(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

    if (!session.getUserId().equals(userId)) {
      throw new IllegalArgumentException("You are not allowed to revoke this session");
    }

    if (currrentSessionId.equals(session.getSessionId())) {
      throw new IllegalStateException("Cannot revoke current session");
    }

    refreshTokenRepository.revokeRefreshToken(session.getRefreshToken());
  }
}
