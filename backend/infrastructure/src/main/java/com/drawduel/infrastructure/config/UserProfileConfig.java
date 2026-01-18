package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.ChangePasswordUseCasePort;
import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.ports.GetSessionInfoUseCasePort;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.usecases.ChangePasswordUseCase;
import com.drawduel.application.usecases.DeleteAccountUseCase;
import com.drawduel.application.usecases.GetProfileUseCase;
import com.drawduel.application.usecases.GetSessionInfoUseCase;
import com.drawduel.application.usecases.UpdateProfileUseCase;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.domain.ports.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProfileConfig {

  @Bean
  public GetSessionInfoUseCasePort getSessionInfoUseCase(
      RefreshTokenRepository refreshTokenRepository) {
    return new GetSessionInfoUseCase(refreshTokenRepository);
  }

  @Bean
  public UpdateProfileUseCasePort updateProfileUseCase(
      UserRepository userRepository, ImageStorageSevicePort imageStorageService) {
    return new UpdateProfileUseCase(userRepository, imageStorageService);
  }

  @Bean
  public ChangePasswordUseCasePort changePasswordUseCase(
      UserRepository userRepository, PasswordHasher passwordHasher) {
    return new ChangePasswordUseCase(userRepository, passwordHasher);
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
