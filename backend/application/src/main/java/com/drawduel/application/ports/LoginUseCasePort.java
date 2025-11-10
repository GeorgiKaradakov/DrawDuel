package com.drawduel.application.ports;

public interface LoginUseCasePort {
  public record Query(
      String identifier, String password, String ipAddress, String userAgent, String location) {}

  public record Result(String accessToken, String refreshToken) {}

  Result handle(Query q);
}
