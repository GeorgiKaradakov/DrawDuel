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
      UUID userId, String ipAdress, String userAgent, String location) {
    UserSession session =
        new UserSession(
            UUID.randomUUID(),
            userId,
            ipAdress,
            userAgent,
            location,
            jwtService.generateRefreshToken(),
            false,
            Instant.now(),
            Instant.now().plus(refreshTokenValidityDays, ChronoUnit.DAYS));

    saveRefreshTokenUseCase.handle(
        new com.drawduel.application.ports.SaveRefreshTokenUseCasePort.Query(session));

    return session.getRefreshToken();
  }

  public String generateAccessToken(UUID userId, String username, String email) {
    return jwtService.generateToken(userId, username, email);
  }

  public String[] generateTokens(User user, String ipAdress, String userAgent, String location) {
    String refreshToken = generateRefreshToken(user.getId(), ipAdress, userAgent, location);
    String accessToken = generateAccessToken(user.getId(), user.getUsername(), user.getEmail());
    return new String[] {accessToken, refreshToken};
  }

  public String[] generateTokens(UserDto user, String ipAdress, String userAgent, String location) {
    String refreshToken = generateRefreshToken(user.getId(), ipAdress, userAgent, location);
    String accessToken = generateAccessToken(user.getId(), user.getUsername(), user.getEmail());
    return new String[] {accessToken, refreshToken};
  }
}
