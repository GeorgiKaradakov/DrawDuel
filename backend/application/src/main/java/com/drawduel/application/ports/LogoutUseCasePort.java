package com.drawduel.application.ports;

public interface LogoutUseCasePort {

  record Query(String refreshToken) {}

  void handle(Query query);
}
