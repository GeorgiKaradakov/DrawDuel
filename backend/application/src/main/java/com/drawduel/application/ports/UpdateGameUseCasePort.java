package com.drawduel.application.ports;

import com.drawduel.application.dtos.GameDto;
import java.util.UUID;

public interface UpdateGameUseCasePort {
  public record Query(UUID gameId, int drawerPoints, int guesserPoints, boolean drawerIsA) {}

  public record Result(GameDto gameDto) {}

  public Result handle(Query query);
}
