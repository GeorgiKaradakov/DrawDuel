package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.ChangePasswordUseCasePort;
import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.ports.DeleteProfileImageUseCasePort;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.ports.GetSessionInfoUseCasePort;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.RevokeSessionUseCasePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.usecases.ChangePasswordUseCase;
import com.drawduel.application.usecases.DeleteAccountUseCase;
import com.drawduel.application.usecases.DeleteProfileImageUseCase;
import com.drawduel.application.usecases.GetProfileUseCase;
import com.drawduel.application.usecases.GetSessionInfoUseCase;
import com.drawduel.application.usecases.RevokeSessionUseCase;
import com.drawduel.application.usecases.UpdateProfileUseCase;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.domain.ports.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProfileConfig {

  @Bean
  public RevokeSessionUseCasePort revokeSessionUseCase(
      RefreshTokenRepository refreshTokenRepository) {
    return new RevokeSessionUseCase(refreshTokenRepository);
  }

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
  public DeleteProfileImageUseCasePort deleteProfileImageUseCase(
      UserRepository userRepository,
      ImageStorageSevicePort imageStorageService,
      @Value("${DEFAULT_PROFILE_IMAGE_URL}") String defaultProfileImageUrl) {
    return new DeleteProfileImageUseCase(
        imageStorageService, userRepository, defaultProfileImageUrl);
  }

  @Bean
  public DeleteAccountUseCasePort deleteAccountUseCase(
      UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
    return new DeleteAccountUseCase(userRepository, refreshTokenRepository);
  }
}
