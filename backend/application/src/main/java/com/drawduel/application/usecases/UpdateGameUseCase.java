package com.drawduel.application.usecases;

import com.drawduel.application.mapper.GameMapper;
import com.drawduel.application.ports.UpdateGameUseCasePort;
import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateGameUseCase implements UpdateGameUseCasePort {
  private final GameRepository gameRepository;
  private final GameMapper mapper;

  @Override
  public Result handle(Query query) {
    Game game =
        gameRepository
            .findById(query.gameId())
            .orElseThrow(() -> new IllegalArgumentException("Game not found"));

    game.addScores(query.drawerPoints(), query.guesserPoints(), query.drawerIsA());
    int nextRound = game.getCurrentRound() + 1;

    if (nextRound > game.getTotalRounds()) {
      int scoreA = game.getPlayerADrawPoints() + game.getPlayerAGuessPoints();
      int scoreB = game.getPlayerBDrawPoints() + game.getPlayerBGuessPoints();

      game.setWinnerId(
          scoreA > scoreB ? game.getPlayerAId() : scoreB > scoreA ? game.getPlayerBId() : null);

      game.setStatus(com.drawduel.domain.enums.GameStatus.FINISHED);
      game.setEndedAt(Instant.now());

    } else {
      game.setCurrentRound(nextRound);
    }

    Game updated = gameRepository.save(game);
    return new Result(mapper.toDto(updated));
  }
}
