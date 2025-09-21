package com.drawduel.application.ports;

import com.drawduel.domain.models.User;
import java.util.UUID;

public interface GetUserByIdUserCasePort {
  public record Query(UUID id) {}

  public record Result(User user) {}

  public Result handle(Query q);
}
