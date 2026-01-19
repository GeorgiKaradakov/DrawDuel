package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.drawduel.domain.models.UserSession;
import com.drawduel.infrastructure.mappers.UserSessionToJpaEntity;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.TokensJpaRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.drawduel.infrastructure.persistence.ports.RefreshTokenRepositoryImpl;
import com.drawduel.tests.BaseIntegrationTest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RefreshTokensRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private TokensJpaRepository tokensJpaRepository;
  @Autowired private UserJpaRepository userJpaRepository;

  private RefreshTokenRepositoryImpl refreshTokenRepository;
  private UUID userId;
  private UUID sessionId;

  @BeforeEach
  void setup() {
    UserSessionToJpaEntity mapper = Mappers.getMapper(UserSessionToJpaEntity.class);
    refreshTokenRepository =
        new RefreshTokenRepositoryImpl(tokensJpaRepository, userJpaRepository, mapper);

    userId = UUID.randomUUID();
    sessionId = UUID.randomUUID();

    String unique = UUID.randomUUID().toString().substring(0, 8);

    JpaUserEntity user = new JpaUserEntity();
    user.setId(userId);
    user.setUsername("testuser_" + unique);
    user.setEmail("user_" + unique + "@test.com");
    user.setPasswordHash("hash123");
    user.setCreatedAt(Instant.now());

    userJpaRepository.save(user);
  }

  @Test
  @Transactional
  void shouldSaveAndFindByRefreshToken() {
    String refreshToken = "refresh-token-123";

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            sessionId,
            "127.0.0.1",
            "JUnit",
            "Earth",
            "Linux",
            refreshToken,
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(session);

    Optional<UserSession> found = refreshTokenRepository.findByRefreshToken(refreshToken);

    assertThat(found).isPresent();
    assertThat(found.get().getRefreshToken()).isEqualTo(refreshToken);
    assertThat(found.get().getUserId()).isEqualTo(userId);
    assertThat(found.get().getSessionId()).isEqualTo(sessionId);
  }

  @Test
  void shouldThrowWhenSavingWithoutExistingUser() {
    UUID fakeUserId = UUID.randomUUID();

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            fakeUserId,
            UUID.randomUUID(),
            "127.0.0.1",
            "JUnit",
            "Earth",
            "Linux",
            "invalid-token",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    assertThrows(
        JpaObjectRetrievalFailureException.class, () -> refreshTokenRepository.save(session));
  }

  @Test
  @Transactional
  void shouldRevokeRefreshToken() {
    String refreshToken = "to-revoke";

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            sessionId,
            "127.0.0.1",
            "JUnit",
            "Mars",
            "Linux",
            refreshToken,
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(session);

    refreshTokenRepository.revokeRefreshToken(refreshToken);

    var updated =
        tokensJpaRepository
            .findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalStateException("Token not found"));

    assertThat(updated.getRevoked()).isTrue();
  }

  // ✅ NEW TESTS BELOW

  @Test
  @Transactional
  void shouldFindActiveSessionsByUserId() {
    UserSession activeSession =
        new UserSession(
            UUID.randomUUID(),
            userId,
            UUID.randomUUID(),
            "127.0.0.1",
            "Firefox",
            "Earth",
            "Linux",
            "active-token",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(activeSession);

    List<UserSession> sessions = refreshTokenRepository.findActiveSessionsByUserId(userId);

    assertThat(sessions).hasSize(1);
    assertThat(sessions.get(0).getRefreshToken()).isEqualTo("active-token");
  }

  @Test
  @Transactional
  void shouldNotReturnRevokedSessions() {
    UserSession revokedSession =
        new UserSession(
            UUID.randomUUID(),
            userId,
            UUID.randomUUID(),
            "127.0.0.1",
            "Chrome",
            "Earth",
            "Linux",
            "revoked-token",
            true,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(revokedSession);

    List<UserSession> sessions = refreshTokenRepository.findActiveSessionsByUserId(userId);

    assertThat(sessions).isEmpty();
  }

  @Test
  @Transactional
  void shouldFindActiveSessionBySessionId() {
    UUID targetSessionId = UUID.randomUUID();

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            targetSessionId,
            "127.0.0.1",
            "Safari",
            "Earth",
            "Linux",
            "session-id-token",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(session);

    Optional<UserSession> found =
        refreshTokenRepository.findActiveSessionBySessionId(targetSessionId);

    assertThat(found).isPresent();
    assertThat(found.get().getSessionId()).isEqualTo(targetSessionId);
  }
}
