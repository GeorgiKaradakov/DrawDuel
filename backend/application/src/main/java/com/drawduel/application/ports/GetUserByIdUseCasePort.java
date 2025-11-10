package com.drawduel.application.ports;

import com.drawduel.application.dtos.UserDto;
import java.util.UUID;

public interface GetUserByIdUseCasePort {
  public record Query(UUID userID) {}

  public record Result(UserDto response) {}

  Result handle(Query q);
}
