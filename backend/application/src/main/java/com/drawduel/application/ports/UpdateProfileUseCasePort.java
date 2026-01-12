package com.drawduel.application.ports;

import java.util.UUID;

public interface UpdateProfileUseCasePort {

  record Query(UUID userId, String username, String email) {}

  record Result(String username, String email) {}

  Result handle(Query query);
}
