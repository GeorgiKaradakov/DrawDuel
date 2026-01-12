package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.usecases.DeleteAccountUseCase;
import com.drawduel.application.usecases.GetProfileUseCase;
import com.drawduel.application.usecases.UpdateProfileUseCase;
import com.drawduel.domain.ports.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProfileConfig {

  @Bean
  public UpdateProfileUseCasePort updateProfileUseCase(UserRepository userRepository) {
    return new UpdateProfileUseCase(userRepository);
  }

  @Bean
  public GetProfileUseCasePort getProfileUseCase(UserRepository userRepository) {
    return new GetProfileUseCase(userRepository);
  }

  @Bean
  public DeleteAccountUseCasePort deleteAccountUseCase(UserRepository userRepository) {
    return new DeleteAccountUseCase(userRepository);
  }
}
