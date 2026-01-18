package com.drawduel.infrastructure.persistence.ports;

import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.infrastructure.mappers.UserSessionToJpaEntity;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.TokensJpaRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
  private final TokensJpaRepository tokensJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private final UserSessionToJpaEntity mapper;

  @Override
  @Transactional
  public void save(UserSession session) {
    JpaTokensEntity entity = mapper.toJpaEntity(session);
    tokensJpaRepository.save(entity);
  }

  @Override
  public Optional<UserSession> findByRefreshToken(String refreshToken) {
    return tokensJpaRepository.findByRefreshToken(refreshToken).map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void revokeRefreshToken(String refreshToken) {
    tokensJpaRepository
        .findByRefreshToken(refreshToken)
        .ifPresent(
            token -> {
              token.setRevoked(true);
              tokensJpaRepository.save(token);
            });
  }

  @Override
  public void deleteByUserId(UUID userId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteByUserId'");
  }
}
