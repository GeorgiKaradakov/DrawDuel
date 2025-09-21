package com.drawduel.application.ports;

import com.drawduel.domain.models.User;

public interface CreateUserUseCasePort {
  record Query(String username, String email, String rawPass) {}

  record Result(User user) {}

  Result handle(Query q);
}
