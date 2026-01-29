package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import com.drawduel.tests.BaseIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class GameRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private GameRepository gameRepository;
  @PersistenceContext private EntityManager entityManager;

  @Test
  void savesAndLoadsGameCorrectly() {
    UUID id = UUID.randomUUID();

    Game game =
        new Game(
            id,
            UUID.randomUUID(),
            UUID.randomUUID(),
            4,
            1,
            GameStatus.IN_PROGRESS,
            null,
            0,
            0,
            0,
            0,
            Instant.now(),
            null);

    gameRepository.save(game);

    Optional<Game> loaded = gameRepository.findById(id);
    assertThat(loaded).isPresent();
  }

  @Test
  void findByIdReturnsEmptyForMissingGame() {
    assertThat(gameRepository.findById(UUID.randomUUID())).isEmpty();
  }

  @Test
  void saveOverwritesExistingGameInDatabase() {
    UUID id = UUID.randomUUID();
    UUID a = UUID.randomUUID();
    UUID b = UUID.randomUUID();

    gameRepository.save(
        new Game(id, a, b, 3, 1, GameStatus.IN_PROGRESS, null, 0, 0, 0, 0, Instant.now(), null));

    gameRepository.save(
        new Game(
            id, a, b, 3, 3, GameStatus.FINISHED, a, 10, 5, 8, 4, Instant.now(), Instant.now()));

    JpaGameEntity entity = entityManager.find(JpaGameEntity.class, id);

    assertThat(entity.getStatus()).isEqualTo(GameStatus.FINISHED);
    assertThat(entity.getWinnerId()).isEqualTo(a);
  }
}
