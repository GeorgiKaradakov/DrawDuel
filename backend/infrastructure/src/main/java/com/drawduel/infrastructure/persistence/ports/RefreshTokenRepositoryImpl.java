package com.drawduel.infrastructure.persistence.ports;

import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
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

  @Override
  @Transactional
  public void save(UserSession session) {
    JpaUserEntity userEntity =
        userJpaRepository
            .findById(session.getUserId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "User with ID " + session.getUserId() + " does not exist"));

    JpaTokensEntity entity = new JpaTokensEntity();
    entity.setId(session.getId());
    entity.setUser(userEntity);
    entity.setRefreshToken(session.getRefreshToken());
    entity.setExpiresAt(session.getExpiresAt());
    entity.setRevoked(session.getRevoked());
    entity.setIpAdress(session.getIpAddress());
    entity.setUserAgent(session.getUserAgent());
    entity.setLocation(session.getLocation());
    entity.setIssuedAt(session.getIssuedAt());

    userEntity.getTokens().add(entity);
    userJpaRepository.save(userEntity);
  }

  @Override
  public Optional<UserSession> findByRefreshToken(String refreshToken) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByRefreshToken'");
  }

  @Override
  public void deleteByUserId(UUID userId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteByUserId'");
  }
}
