package com.drawduel.infrastructure.persistence.jpa.repositories;

import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensJpaRepository extends JpaRepository<JpaTokensEntity, UUID> {
  @Transactional
  Optional<JpaTokensEntity> findByRefreshToken(String token);

  Optional<JpaTokensEntity> findByUserId(UUID userId);

  @Transactional
  @Modifying
  @Query("DELETE FROM JpaTokensEntity t WHERE t.user.id = :userId")
  void deleteAllByUserId(UUID userId);

  @Query(
      """
        SELECT t
        FROM JpaTokensEntity t
        WHERE t.user.id = :userId
          AND t.revoked = false
          AND t.expiresAt > CURRENT_TIMESTAMP
        ORDER BY t.issuedAt DESC
      """)
  List<JpaTokensEntity> findActiveSessionsByUserId(UUID userId);

  @Query(
      """
      SELECT t FROM JpaTokensEntity t
      WHERE t.sessionId = :sessionId
        AND t.revoked = false
        AND t.expiresAt > CURRENT_TIMESTAMP
      """)
  Optional<JpaTokensEntity> findActiveSessionBySessionId(UUID sessionId);

  @Transactional
  @Modifying
  @Query("DELETE FROM JpaTokensEntity t WHERE t.refreshToken = :token")
  void deleteByRefreshToken(String token);

  @Transactional
  @Modifying
  @Query("DELETE FROM JpaTokensEntity t WHERE t.expiresAt < :now")
  void deleteAllExpiredTokens(Instant now);

  boolean existsByRefreshToken(String token);
}
