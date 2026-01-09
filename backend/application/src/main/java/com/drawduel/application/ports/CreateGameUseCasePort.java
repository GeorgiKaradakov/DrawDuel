package com.drawduel.application.ports;

import com.drawduel.application.dtos.GameDto;
import java.util.UUID;

public interface CreateGameUseCasePort {
  public record Query(UUID playerAId, UUID playerBId) {}

  public record Result(GameDto gameDto) {}

  public Result handle(Query query);
}
