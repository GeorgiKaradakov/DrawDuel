package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.drawduel.domain.models.User;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.drawduel.infrastructure.persistence.ports.UserRepositoryImpl;
import com.drawduel.tests.BaseIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class UserRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private UserJpaRepository userJpaRepository;
  @PersistenceContext private EntityManager entityManager;

  private UserRepositoryImpl userRepository;

  @BeforeEach
  void setup() {
    userRepository = new UserRepositoryImpl(userJpaRepository);
  }

  @Test
  void savesAndFindsUserById_andPersistsToDatabase() {
    UUID id = UUID.randomUUID();

    User user = new User(id, "guts", "guts@test.com", "hash", Instant.now(), null);

    userRepository.save(user);

    JpaUserEntity entity = entityManager.find(JpaUserEntity.class, id);

    assertThat(entity).isNotNull();
    assertThat(entity.getUsername()).isEqualTo("guts");
  }

  @Test
  void duplicateEmailShouldFail() {
    userRepository.save(new User(UUID.randomUUID(), "a", "dup@test.com", "h", Instant.now(), null));
    entityManager.flush();

    assertThatThrownBy(
            () -> {
              userRepository.save(
                  new User(UUID.randomUUID(), "b", "dup@test.com", "h", Instant.now(), null));
              entityManager.flush();
            })
        .isInstanceOf(Exception.class);
  }

  @Test
  void updateProfileImageUrlThrowsUnsupportedOperation() {
    UUID id = UUID.randomUUID();

    userRepository.save(new User(id, "img", "img@test.com", "h", Instant.now(), null));

    assertThatThrownBy(() -> userRepository.updateProfileImageUrl(id, "http://image"))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void deleteByIdRemovesUserFromDatabase() {
    UUID id = UUID.randomUUID();

    userRepository.save(new User(id, "del", "del@test.com", "h", Instant.now(), null));

    userRepository.deleteById(id);

    assertThat(entityManager.find(JpaUserEntity.class, id)).isNull();
  }
}
