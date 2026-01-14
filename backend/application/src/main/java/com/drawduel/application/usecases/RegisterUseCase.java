package com.drawduel.application.usecases;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUseCase implements RegisterUseCasePort {
  private final UserRepository userRepo;
  private final PasswordHasher hasher;
  private final TokensService tokensService;
  private final ImageStorageSevicePort imageStorage;

  @Override
  public Result handle(Query q) {
    RegisterRequestDto req = q.request();

    userRepo
        .findByEmail(req.getEmail())
        .ifPresent(
            u -> {
              throw new IllegalArgumentException("Email already in use");
            });

    userRepo
        .findByUsername(req.getUsername())
        .ifPresent(
            u -> {
              throw new IllegalArgumentException("Username already in use");
            });

    if (!req.getPass().equals(req.getRepeatPass())) {
      throw new IllegalArgumentException("Passwords do not match");
    }

    UUID userId = UUID.randomUUID();
    String profileImageUrl = null;
    if (req.getProfileImageBase64() != null) {
      profileImageUrl = imageStorage.uploadProfileImage(req.getProfileImageBase64(), userId);
    }

    String hash = hasher.hash(req.getPass());
    User user =
        new User(userId, req.getUsername(), req.getEmail(), hash, Instant.now(), profileImageUrl);
    userRepo.save(user);

    String[] tokens =
        tokensService.generateTokens(
            user, req.getIpAddress(), req.getUserAgent(), req.getLocation());

    return new Result(new AuthResponseDto(tokens[0], tokens[1]));
  }
}
