package com.drawduel.application.usecases;

import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;

public class UpdateProfileUseCase implements UpdateProfileUseCasePort {

  private final UserRepository userRepository;

  public UpdateProfileUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Result handle(Query query) {
    User user =
        userRepository
            .findById(query.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    userRepository
        .findByUsername(query.username())
        .ifPresent(
            existing -> {
              if (!existing.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Username already in use");
              }
            });

    userRepository
        .findByEmail(query.email())
        .ifPresent(
            existing -> {
              if (!existing.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Email already in use");
              }
            });

    user.setUsername(query.username());
    user.setEmail(query.email());

    userRepository.save(user);

    return new Result(user.getUsername(), user.getEmail());
  }
}
