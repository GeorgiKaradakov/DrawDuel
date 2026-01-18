package com.drawduel.application.ports;

import java.util.UUID;

public interface RevokeSessionUseCasePort {

  record Query(UUID userId, UUID sessionId, UUID currentSessionId) {}

  void handle(Query query);
}
