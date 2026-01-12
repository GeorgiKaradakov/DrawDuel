package com.drawduel.application.ports;

import java.util.UUID;

public interface DeleteAccountUseCasePort {

  record Query(UUID userId) {}

  void handle(Query query);
}
