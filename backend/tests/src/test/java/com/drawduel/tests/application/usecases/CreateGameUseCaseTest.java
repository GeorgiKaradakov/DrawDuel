package com.drawduel.tests.application.usecases;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.GameDto;
import com.drawduel.application.mapper.GameMapper;
import com.drawduel.application.ports.CreateGameUseCasePort;
import com.drawduel.application.usecases.CreateGameUseCase;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateGameUseCaseTest {

  private GameRepository repository;
  private GameMapper mapper;
  private CreateGameUseCase useCase;

  private UUID playerA;
  private UUID playerB;

  @BeforeEach
  void setUp() {
    repository = mock(GameRepository.class);
    mapper = mock(GameMapper.class);
    useCase = new CreateGameUseCase(repository, mapper);

    playerA = UUID.randomUUID();
    playerB = UUID.randomUUID();
  }

  @Test
  void should_create_game_in_progress() {
    Game savedGame = new Game();
    savedGame.setId(UUID.randomUUID());
    savedGame.setStatus(GameStatus.IN_PROGRESS);

    when(repository.save(any())).thenReturn(savedGame);
    when(mapper.toDto(any())).thenReturn(mock(GameDto.class));

    var result = useCase.handle(new CreateGameUseCasePort.Query(playerA, playerB));

    verify(repository).save(any(Game.class));
    assertNotNull(result.gameDto());
  }
}
