package com.drawduel.application.ports;

import com.drawduel.application.dtos.UserDto;
import java.util.UUID;

public interface GetUserInfoUseCasePort {
  public record Query(UUID userId) {}

  public record Result(UserDto response) {}

  Result handle(Query query);
}
