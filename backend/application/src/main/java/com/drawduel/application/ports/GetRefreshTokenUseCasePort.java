package com.drawduel.application.ports;

import com.drawduel.application.dtos.UserSessionDto;

public interface GetRefreshTokenUseCasePort {
  record Query(String refreshToken) {}

  record Result(UserSessionDto sessionDto) {}

  Result handle(Query q);
}
