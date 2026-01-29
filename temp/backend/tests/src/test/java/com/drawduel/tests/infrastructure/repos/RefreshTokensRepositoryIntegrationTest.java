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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class RefreshTokensRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private TokensJpaRepository tokensJpaRepository;
  @Autowired private UserJpaRepository userJpaRepository;
  @PersistenceContext private EntityManager entityManager;

  private RefreshTokenRepositoryImpl refreshTokenRepository;
  private UUID userId;

  @BeforeEach
  void setup() {
    refreshTokenRepository =
        new RefreshTokenRepositoryImpl(
            tokensJpaRepository,
            userJpaRepository,
            Mappers.getMapper(UserSessionToJpaEntity.class));

    userId = UUID.randomUUID();

    JpaUserEntity user = new JpaUserEntity();
    user.setId(userId);
    user.setUsername("user_" + userId);
    user.setEmail("user_" + userId + "@test.com");
    user.setPasswordHash("hash");
    user.setCreatedAt(Instant.now());

    entityManager.persist(user);
    entityManager.flush();
  }

  @Test
  @Transactional
  void deleteByUserIdRemovesAllTokensFromDatabase() {
    saveToken("t1");
    saveToken("t2");

    refreshTokenRepository.deleteByUserId(userId);

    Long count =
        entityManager
            .createQuery("SELECT COUNT(t) FROM JpaTokensEntity t WHERE t.user.id = :id", Long.class)
            .setParameter("id", userId)
            .getSingleResult();

    assertThat(count).isZero();
  }

  @Test
  void deleteByUserIdDoesNothingWhenNoTokensExist() {
    refreshTokenRepository.deleteByUserId(userId);

    Long count =
        entityManager
            .createQuery("SELECT COUNT(t) FROM JpaTokensEntity t WHERE t.user.id = :id", Long.class)
            .setParameter("id", userId)
            .getSingleResult();

    assertThat(count).isZero();
  }

  @Test
  void savingTokenForMissingUserThrows() {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "127.0.0.1",
            "JUnit",
            "Earth",
            "Linux",
            "invalid",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    assertThrows(
        JpaObjectRetrievalFailureException.class, () -> refreshTokenRepository.save(session));
  }

  private void saveToken(String token) {
    refreshTokenRepository.save(
        new UserSession(
            UUID.randomUUID(),
            userId,
            UUID.randomUUID(),
            "127.0.0.1",
            "JUnit",
            "Earth",
            "Linux",
            token,
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600)));
  }
}
