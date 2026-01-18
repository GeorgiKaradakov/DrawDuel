package com.drawduel.application.ports;

import com.drawduel.application.dtos.DevicesResponseDto;
import java.util.UUID;

public interface GetSessionInfoUseCasePort {
  public record Query(UUID userId, String ipAddress) {}

  public record Result(DevicesResponseDto devices) {}

  Result handle(Query query);
}
