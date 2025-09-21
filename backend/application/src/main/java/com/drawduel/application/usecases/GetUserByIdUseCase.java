package com.drawduel.application.usecases;

import com.drawduel.application.ports.GetUserByIdUserCasePort;
import com.drawduel.domain.ports.UserRepository;

public class GetUserByIdUseCase implements GetUserByIdUserCasePort {
  private final UserRepository userRepo;

  public GetUserByIdUseCase(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public Result handle(Query q) {
    return new Result(
        userRepo
            .findById(q.id())
            .orElseThrow(() -> new IllegalArgumentException("user does not exists")));
  }
}
