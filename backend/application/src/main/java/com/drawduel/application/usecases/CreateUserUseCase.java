package com.drawduel.application.usecases;

import com.drawduel.application.ports.CreateUserUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.UUID;

public class CreateUserUseCase implements CreateUserUseCasePort {
  private final UserRepository userRepo;
  private final PasswordHasher hasher;

  public CreateUserUseCase(UserRepository userRepo, PasswordHasher hasher) {
    this.userRepo = userRepo;
    this.hasher = hasher;
  }

  @Override
  public Result handle(Query cmd) {
    if (cmd.username() == null || cmd.username().isBlank())
      throw new IllegalArgumentException("username is required");

    if (cmd.email() == null || cmd.email().isBlank())
      throw new IllegalArgumentException("email is required");

    System.out.println(cmd.rawPass().length());
    if (cmd.rawPass() == null || cmd.rawPass().length() < 6)
      throw new IllegalArgumentException("password must be atleast 6 characters long");

    userRepo
        .findByUsername(cmd.username())
        .ifPresent(
            x -> {
              throw new IllegalStateException("username already taken");
            });

    userRepo
        .findByEmail(cmd.email())
        .ifPresent(
            x -> {
              throw new IllegalStateException("email already taken");
            });

    String hash = hasher.hash(cmd.rawPass());
    User user = new User(UUID.randomUUID(), cmd.username(), cmd.email(), hash, Instant.now());

    userRepo.save(user);

    return new Result(user);
  }
}
