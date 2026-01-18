package com.drawduel.application.services;

import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.models.UserSession;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokensService {
  private final JwtService jwtService;
  private final SaveRefreshTokenUseCasePort saveRefreshTokenUseCase;
  private final Integer refreshTokenValidityDays;

  private String generateRefreshToken(
      UUID userId,
      UUID sessionId,
      String ipAdress,
      String userAgent,
      String location,
      String osName) {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            sessionId,
            ipAdress,
            userAgent,
            location,
            osName,
            jwtService.generateRefreshToken(),
            false,
            Instant.now(),
            Instant.now().plus(refreshTokenValidityDays, ChronoUnit.DAYS));

    saveRefreshTokenUseCase.handle(
        new com.drawduel.application.ports.SaveRefreshTokenUseCasePort.Query(session));

    return session.getRefreshToken();
  }

  public String generateAccessToken(UUID userId, UUID sessionId) {
    return jwtService.generateToken(userId, sessionId);
  }

  public String[] generateTokens(
      User user, String ipAdress, String userAgent, String location, String osName) {
    UUID sessionId = UUID.randomUUID();
    String refreshToken =
        generateRefreshToken(user.getId(), sessionId, ipAdress, userAgent, location, osName);
    String accessToken = generateAccessToken(user.getId(), sessionId);
    return new String[] {accessToken, refreshToken};
  }

  public String[] generateTokens(
      UserDto user,
      UUID sessionId,
      String ipAdress,
      String userAgent,
      String location,
      String osName) {
    String refreshToken =
        generateRefreshToken(user.getId(), sessionId, ipAdress, userAgent, location, osName);
    String accessToken = generateAccessToken(user.getId(), sessionId);
    return new String[] {accessToken, refreshToken};
  }
}
