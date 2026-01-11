package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import com.drawduel.tests.BaseIntegrationTest;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GameRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired GameRepository gameRepository;

  @Test
  void savesAndLoadsGameCorrectly() {
    UUID gameId = UUID.randomUUID();
    UUID playerA = UUID.randomUUID();
    UUID playerB = UUID.randomUUID();

    Game game =
        new Game(
            gameId,
            playerA,
            playerB,
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

    Optional<Game> loaded = gameRepository.findById(gameId);

    assertThat(loaded).isPresent();
    assertThat(loaded.get().getPlayerAId()).isEqualTo(playerA);
    assertThat(loaded.get().getPlayerBId()).isEqualTo(playerB);
    assertThat(loaded.get().getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
  }

  @Test
  void persistsWinnerAndEndTime() {
    UUID gameId = UUID.randomUUID();
    UUID playerA = UUID.randomUUID();
    UUID playerB = UUID.randomUUID();

    Game finished =
        new Game(
            gameId,
            playerA,
            playerB,
            4,
            4,
            GameStatus.FINISHED,
            playerA,
            50,
            20,
            30,
            10,
            Instant.now(),
            Instant.now());

    gameRepository.save(finished);

    Game loaded = gameRepository.findById(gameId).orElseThrow();

    assertThat(loaded.getWinnerId()).isEqualTo(playerA);
    assertThat(loaded.getEndedAt()).isNotNull();
  }
}
