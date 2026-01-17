package com.drawduel.application.ports;

import java.util.UUID;

public interface UpdateProfileUseCasePort {

  record Query(UUID userId, String username, String email, byte[] profileImage) {}

  record Result(String username, String email, String profileImageUrl) {}

  Result handle(Query query);
}
