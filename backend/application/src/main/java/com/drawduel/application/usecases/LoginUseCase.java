package com.drawduel.application.usecases;

import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;

public class LoginUseCase implements LoginUseCasePort {

  private final UserRepository userRepo;
  private final TokensService tokensService;
  private final PasswordHasher hasher;

  public LoginUseCase(UserRepository userRepo, TokensService tokensService, PasswordHasher hasher) {
    this.userRepo = userRepo;
    this.tokensService = tokensService;
    this.hasher = hasher;
  }

  @Override
  public Result handle(Query q) {
    Optional<User> optUser = userRepo.findByEmail(q.identifier());
    if (optUser.isEmpty()) {
      optUser = userRepo.findByUsername(q.identifier());
      if (optUser.isEmpty()) {
        throw new IllegalArgumentException("Invalid credentials");
      }
    }

    User user = optUser.get();

    Boolean passMatch = hasher.matches(q.password(), user.getPassHash());
    if (!passMatch) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    String[] tokens =
        tokensService.generateTokens(user, q.ipAddress(), q.userAgent(), q.location());
    return new Result(tokens[0], tokens[1]);
  }
}
