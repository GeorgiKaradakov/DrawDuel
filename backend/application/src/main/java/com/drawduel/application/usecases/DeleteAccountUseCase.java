package com.drawduel.application.usecases;

import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.domain.ports.RefreshTokenRepository;
import com.drawduel.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteAccountUseCase implements DeleteAccountUseCasePort {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void handle(Query query) {
    UUID userId = query.userId();

    userRepository
        .findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    refreshTokenRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }
}
