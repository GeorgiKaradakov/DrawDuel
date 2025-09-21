package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.CreateUserUseCasePort;
import com.drawduel.application.ports.GetUserByIdUserCasePort;
import com.drawduel.application.usecases.CreateUserUseCase;
import com.drawduel.application.usecases.GetUserByIdUseCase;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCasesConfig {

  @Bean
  public CreateUserUseCasePort createUserUserCase(UserRepository userRepo, PasswordHasher hasher) {
    return new CreateUserUseCase(userRepo, hasher);
  }

  @Bean
  public GetUserByIdUserCasePort getUserByIdUseCase(UserRepository userRepo) {
    return new GetUserByIdUseCase(userRepo);
  }
}
