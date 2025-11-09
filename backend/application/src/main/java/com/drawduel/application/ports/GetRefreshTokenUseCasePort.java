package com.drawduel.application.ports;

import com.drawduel.application.dtos.UserSessionDto;

public interface GetRefreshTokenUseCasePort {
  record Query(String refreshToken) {}

  record Result(UserSessionDto response) {}

  Result handle(Query q);
}
