package com.drawduel.application.usecases;

import com.drawduel.application.dtos.UserProfileDto;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.domain.ports.UserRepository;

public class GetProfileUseCase implements GetProfileUseCasePort {

  private final UserRepository userRepository;

  public GetProfileUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Result handle(Query query) {
    var user =
        userRepository
            .findById(query.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new Result(new UserProfileDto(user.getUsername(), user.getEmail()));
  }
}
