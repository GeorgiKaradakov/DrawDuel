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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RefreshTokensRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private TokensJpaRepository tokensJpaRepository;
  @Autowired private UserJpaRepository userJpaRepository;
  @Autowired private Environment environment;

  private RefreshTokenRepositoryImpl refreshTokenRepository;
  private UUID userId;

  @BeforeEach
  void setup() {
    UserSessionToJpaEntity mapper = Mappers.getMapper(UserSessionToJpaEntity.class);
    refreshTokenRepository =
        new RefreshTokenRepositoryImpl(tokensJpaRepository, userJpaRepository, mapper);

    // Prepare test user
    userId = UUID.randomUUID();
    JpaUserEntity user = new JpaUserEntity();
    user.setId(userId);
    user.setUsername("testuser");
    user.setEmail("user@test.com");
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
            "127.0.0.1",
            "JUnit",
            "Earth",
            refreshToken,
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(session);

    Optional<UserSession> found = refreshTokenRepository.findByRefreshToken(refreshToken);
    assertThat(found).isPresent();
    assertThat(found.get().getRefreshToken()).isEqualTo(refreshToken);
    assertThat(found.get().getUserId()).isEqualTo(userId);
  }

  @Test
  void shouldThrowWhenSavingWithoutExistingUser() {
    UUID fakeUserId = UUID.randomUUID();

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            fakeUserId,
            "127.0.0.1",
            "JUnit",
            "Earth",
            "invalidtoken",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    assertThrows(IllegalArgumentException.class, () -> refreshTokenRepository.save(session));
  }

  @Test
  @Transactional
  void shouldRevokeRefreshToken() {
    String refreshToken = "to-revoke";

    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            "127.0.0.1",
            "JUnit",
            "Mars",
            refreshToken,
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    refreshTokenRepository.save(session);

    // Revoke the token
    refreshTokenRepository.revokeRefreshToken(refreshToken);

    var updated =
        tokensJpaRepository
            .findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalStateException("Token not found"));

    assertThat(updated.getRevoked()).isTrue();
  }
}
