package com.drawduel.application.usecases;

import com.drawduel.application.mapper.GameMapper;
import com.drawduel.application.ports.CreateGameUseCasePort;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateGameUseCase implements CreateGameUseCasePort {

  private final GameRepository gameRepository;
  private final GameMapper mapper;

  @Override
  public Result handle(Query query) {
    Game game = new Game();
    game.setId(UUID.randomUUID());
    game.setPlayerAId(query.playerAId());
    game.setPlayerBId(query.playerBId());
    game.setTotalRounds(4);
    game.setCurrentRound(1);
    game.setStatus(GameStatus.IN_PROGRESS);
    game.setStartedAt(Instant.now());

    Game saved = gameRepository.save(game);
    return new Result(mapper.toDto(saved));
  }
}
