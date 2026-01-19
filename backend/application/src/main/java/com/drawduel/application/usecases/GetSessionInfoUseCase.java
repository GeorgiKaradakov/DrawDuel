package com.drawduel.application.usecases;

import com.drawduel.application.dtos.DeviceEntryDto;
import com.drawduel.application.dtos.DevicesResponseDto;
import com.drawduel.application.ports.GetSessionInfoUseCasePort;
import com.drawduel.domain.enums.DeviceStatus;
import com.drawduel.domain.models.UserSession;
import com.drawduel.domain.ports.RefreshTokenRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class GetSessionInfoUseCase implements GetSessionInfoUseCasePort {
  RefreshTokenRepository refreshTokenRepository;

  @Override
  public Result handle(Query query) {
    List<UserSession> sessions = refreshTokenRepository.findActiveSessionsByUserId(query.userId());

    List<DeviceEntryDto> devices =
        sessions.stream()
            .map(
                session -> {
                  return new DeviceEntryDto(
                      session.getSessionId(),
                      session.getOsName(),
                      session.getUserAgent(),
                      session.getLocation(),
                      (session.getSessionId().equals(query.sessionId()))
                          ? DeviceStatus.CURRENT_SESSION
                          : DeviceStatus.ACTIVE);
                })
            .toList();

    return new Result(new DevicesResponseDto(devices));
  }
}
