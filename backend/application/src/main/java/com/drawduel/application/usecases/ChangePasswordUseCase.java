package com.drawduel.application.usecases;

import com.drawduel.application.ports.ChangePasswordUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangePasswordUseCase implements ChangePasswordUseCasePort {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  @Override
  public void handle(Query query) {
    UUID userId = query.userId();
    String currentPass = query.currentPassword();
    String newPass = query.newPassword();

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordHasher.matches(currentPass, user.getPassHash()))
      throw new IllegalArgumentException("Incorrect password!");

    String newHashedPass = passwordHasher.hash(newPass);
    user.setPassHash(newHashedPass);

    userRepository.save(user);
  }
}
