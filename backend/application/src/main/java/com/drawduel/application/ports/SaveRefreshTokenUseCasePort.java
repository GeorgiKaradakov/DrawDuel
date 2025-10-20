package com.drawduel.application.ports;

import com.drawduel.domain.models.UserSession;

public interface SaveRefreshTokenUseCasePort {

  public record Query(UserSession session) {}

  public void handle(Query q);
}
