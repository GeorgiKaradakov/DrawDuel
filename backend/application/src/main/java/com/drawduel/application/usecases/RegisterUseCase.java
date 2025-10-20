package com.drawduel.application.usecases;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.RegisterUserCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;

public class RegisterUseCase implements RegisterUserCasePort {
  private final UserRepository userRepo;
  private final PasswordHasher hasher;
  private final TokensService tokensService;

  public RegisterUseCase(
      UserRepository userRepo, PasswordHasher hasher, TokensService tokensService) {
    this.userRepo = userRepo;
    this.hasher = hasher;
    this.tokensService = tokensService;
  }

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

    if (req.getPass() != req.getRepeatPass()) {
      throw new IllegalArgumentException("Passwords do not match");
    }

    String hash = hasher.hash(req.getPass());
    User user = new User(req.getUsername(), req.getEmail(), hash, Instant.now());
    userRepo.save(user);

    return new Result(
        new AuthResponseDto(
            tokensService.generateTokens(
                user, req.getIpAddress(), req.getUserAgent(), req.getLocation())));
  }
}
