package com.drawduel.infrastructure.config;

import com.drawduel.application.mapper.GameMapper;
import com.drawduel.application.ports.CreateGameUseCasePort;
import com.drawduel.application.ports.UpdateGameUseCasePort;
import com.drawduel.application.usecases.CreateGameUseCase;
import com.drawduel.application.usecases.UpdateGameUseCase;
import com.drawduel.domain.ports.GameRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.GameJpaRepository;
import com.drawduel.infrastructure.persistence.ports.GameRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {
  @Bean
  public GameRepository gameRepository(GameJpaRepository jpaRepository) {
    return new GameRepositoryImpl(jpaRepository);
  }

  @Bean
  public CreateGameUseCasePort createGameUseCase(GameRepository gameRepository, GameMapper mapper) {
    return new CreateGameUseCase(gameRepository, mapper);
  }

  @Bean
  public UpdateGameUseCasePort updateGameUseCase(GameRepository gameRepository, GameMapper mapper) {
    return new UpdateGameUseCase(gameRepository, mapper);
  }
}
