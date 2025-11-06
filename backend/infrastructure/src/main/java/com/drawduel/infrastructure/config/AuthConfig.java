package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.application.services.JwtService;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.GetRefreshTokenUseCase;
import com.drawduel.application.usecases.GetUserByIdUseCase;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.application.usecases.SaveRefreshTokenUseCase;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.infrastructure.persistence.ports.UserRepositoryImpl;
import com.drawduel.infrastructure.persistence.security.BCryptPasswordHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

  @Bean
  public JwtService jwtService(
      @Value("${JWT_SECRET}") String key, @Value("${JWT_EXPIRATION_TIME}") Long expirationMs) {
    return new JwtService(key, expirationMs);
  }

  @Bean
  public TokensService tokensService(
      @Value("${REFRESH_TOKEN_EXPIRATION_DAYS}") Integer refreshTokenExpirationDays,
      JwtService jwtService,
      SaveRefreshTokenUseCasePort saveRefreshTokenUseCase) {
    return new TokensService(jwtService, saveRefreshTokenUseCase, refreshTokenExpirationDays);
  }

  @Bean
  public GetUserByIdUseCasePort getUserByIdUseCase(UserRepositoryImpl userRepo) {
    return new GetUserByIdUseCase(userRepo);
  }

  @Bean
  public GetRefreshTokenUseCasePort getRefreshTokenUseCase(
      RefreshTokenRepository refreshTokenRepository,
      com.drawduel.application.mapper.UserSessionDomainToDtoMapper mapper) {
    return new GetRefreshTokenUseCase(refreshTokenRepository, mapper);
  }

  @Bean
  public RegisterUseCasePort registerUseCase(
      UserRepositoryImpl userRepo, BCryptPasswordHasher hasher, TokensService tokenService) {
    return new RegisterUseCase(userRepo, hasher, tokenService);
  }

  @Bean
  public LoginUseCasePort loginUseCase(
      UserRepositoryImpl userRepo, TokensService tokensService, BCryptPasswordHasher hasher) {
    return new LoginUseCase(userRepo, tokensService, hasher);
  }

  @Bean
  public SaveRefreshTokenUseCasePort saveRefreshTokenUseCase(
      RefreshTokenRepository refreshTokenRepository) {
    return new SaveRefreshTokenUseCase(refreshTokenRepository);
  }
}
