package com.drawduel.application.ports;

public interface LoginUseCasePort {
  record Query(
      String identifier, String rawPass, String ipAddress, String userAgent, String location) {}

  record Result(String accessToken) {}

  Result handle(Query q);
}
