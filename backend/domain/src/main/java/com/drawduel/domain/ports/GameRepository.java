package com.drawduel.domain.ports;

import com.drawduel.domain.models.Game;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {
  Game save(Game game);

  Optional<Game> findById(UUID Id);
}
