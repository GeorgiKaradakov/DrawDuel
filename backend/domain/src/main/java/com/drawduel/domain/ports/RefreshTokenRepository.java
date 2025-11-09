package com.drawduel.domain.ports;

import com.drawduel.domain.models.UserSession;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

  void save(UserSession session);

  Optional<UserSession> findByRefreshToken(String refreshToken);

  void revokeRefreshToken(String refreshToken);

  void deleteByUserId(UUID userId);
}
