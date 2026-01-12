package com.drawduel.tests.application.usecases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.GameDto;
import com.drawduel.application.mapper.GameMapper;
import com.drawduel.application.ports.UpdateGameUseCasePort;
import com.drawduel.application.usecases.UpdateGameUseCase;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateGameUseCaseTest {

  private GameRepository repository;
  private GameMapper mapper;
  private UpdateGameUseCase useCase;

  private UUID gameId;
  private UUID playerA;
  private UUID playerB;

  @BeforeEach
  void setUp() {
    repository = mock(GameRepository.class);
    mapper = mock(GameMapper.class);
    useCase = new UpdateGameUseCase(repository, mapper);

    gameId = UUID.randomUUID();
    playerA = UUID.randomUUID();
    playerB = UUID.randomUUID();
  }

  @Test
  void should_finish_game_and_set_winner() {
    Game game =
        new Game(
            gameId,
            playerA,
            playerB,
            1,
            1,
            GameStatus.IN_PROGRESS,
            null,
            10,
            10,
            5,
            5,
            Instant.now(),
            null);

    when(repository.findById(gameId)).thenReturn(Optional.of(game));
    when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(mapper.toDto(any())).thenReturn(mock(GameDto.class));

    var result = useCase.handle(new UpdateGameUseCasePort.Query(gameId, 10, 5, true));

    assertEquals(GameStatus.FINISHED, game.getStatus());
    assertEquals(playerA, game.getWinnerId());
  }
}
