package com.drawduel.application.ports;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;

public interface RegisterUserCasePort {
  public record Query(RegisterRequestDto request) {}

  public record Result(AuthResponseDto response) {}

  public Result handle(Query q);
}
