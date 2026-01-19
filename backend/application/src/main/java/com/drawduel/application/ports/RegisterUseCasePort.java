package com.drawduel.application.ports;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;

public interface RegisterUseCasePort {
  public record Query(RegisterRequestDto request, byte[] imageBytes) {
    public Query(RegisterRequestDto requestDto) {
      this(requestDto, null);
    }
  }

  public record Result(AuthResponseDto response) {}

  public Result handle(Query q);
}
