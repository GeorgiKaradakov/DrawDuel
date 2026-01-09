package com.drawduel.infrastructure.persistence.ports;

import com.drawduel.domain.models.Game;
import com.drawduel.domain.ports.GameRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.GameJpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepositoryImpl implements GameRepository {

  private final GameJpaRepository jpaRepository;

  public GameRepositoryImpl(GameJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Game save(Game game) {
    JpaGameEntity entity = new JpaGameEntity();
    entity.setId(game.getId());
    entity.setPlayerAId(game.getPlayerAId());
    entity.setPlayerBId(game.getPlayerBId());
    entity.setTotalRounds(game.getTotalRounds());
    entity.setCurrentRound(game.getCurrentRound());
    entity.setStatus(game.getStatus());
    entity.setWinnerId(game.getWinnerId());
    entity.setPlayerADrawPoints(game.getPlayerADrawPoints());
    entity.setPlayerBDrawPoints(game.getPlayerBDrawPoints());
    entity.setPlayerAGuessPoints(game.getPlayerAGuessPoints());
    entity.setPlayerBGuessPoints(game.getPlayerBGuessPoints());
    entity.setStartedAt(game.getStartedAt());
    entity.setEndedAt(game.getEndedAt());

    JpaGameEntity saved = jpaRepository.save(entity);
    return new Game(
        saved.getId(),
        saved.getPlayerAId(),
        saved.getPlayerBId(),
        saved.getTotalRounds(),
        saved.getCurrentRound(),
        saved.getStatus(),
        saved.getWinnerId(),
        saved.getPlayerADrawPoints(),
        saved.getPlayerAGuessPoints(),
        saved.getPlayerBDrawPoints(),
        saved.getPlayerBGuessPoints(),
        saved.getStartedAt(),
        saved.getEndedAt());
  }

  @Override
  public Optional<Game> findById(UUID id) {
    return jpaRepository.findById(id).map(this::toDomain);
  }

  private Game toDomain(JpaGameEntity entity) {
    return new Game(
        entity.getId(),
        entity.getPlayerAId(),
        entity.getPlayerBId(),
        entity.getTotalRounds(),
        entity.getCurrentRound(),
        entity.getStatus(),
        entity.getWinnerId(),
        entity.getPlayerADrawPoints(),
        entity.getPlayerAGuessPoints(),
        entity.getPlayerBDrawPoints(),
        entity.getPlayerBGuessPoints(),
        entity.getStartedAt(),
        entity.getEndedAt());
  }
}
