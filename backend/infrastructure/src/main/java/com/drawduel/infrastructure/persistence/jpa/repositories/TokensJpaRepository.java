package com.drawduel.infrastructure.persistence.jpa.repositories;

import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import jakarta.transaction.Transactional;
import java.time.Instant;
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

  // @Transactional
  // @Modifying
  // @Query("DELETE FROM JpaTokensEntity t WHERE t.userId = :userId")
  // void deleteByUserId(UUID userId);

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
