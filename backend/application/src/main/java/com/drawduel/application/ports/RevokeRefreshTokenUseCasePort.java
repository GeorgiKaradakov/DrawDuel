package com.drawduel.application.ports;

public interface RevokeRefreshTokenUseCasePort {

  public record Query(String refreshToken) {}

  public void handle(Query c);
}
