package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUserCasePort;
import com.drawduel.application.ports.SaveRefreshTokenUseCasePort;
import com.drawduel.application.services.JwtService;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.application.usecases.SaveRefreshTokenUseCase;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.domain.ports.UserRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.drawduel.infrastructure.persistence.ports.UserRepositoryImpl;
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
  public UserRepository userRepository(UserJpaRepository userJpaRepository) {
    return new UserRepositoryImpl(userJpaRepository);
  }

  @Bean
  public RegisterUserCasePort registerUseCase(
      UserRepository userRepo, PasswordHasher hasher, TokensService tokenService) {
    return new RegisterUseCase(userRepo, hasher, tokenService);
  }

  @Bean
  public LoginUseCasePort loginUseCase(
      UserRepository userRepo, TokensService tokensService, PasswordHasher hasher) {
    return new LoginUseCase(userRepo, tokensService, hasher);
  }

  @Bean
  public SaveRefreshTokenUseCasePort saveRefreshTokenUseCase(
      RefreshTokenRepository refreshTokenRepository) {
    return new SaveRefreshTokenUseCase(refreshTokenRepository);
  }
}
