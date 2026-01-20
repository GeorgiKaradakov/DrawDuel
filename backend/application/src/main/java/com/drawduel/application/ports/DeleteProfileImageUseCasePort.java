package com.drawduel.application.ports;

import java.util.UUID;

public interface DeleteProfileImageUseCasePort {
  public record Query(UUID userId) {}

  public record Result(String profileImageUrl) {}

  Result handle(Query query);
}
