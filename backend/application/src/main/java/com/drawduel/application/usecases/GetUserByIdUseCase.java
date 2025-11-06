package com.drawduel.application.usecases;

import com.drawduel.application.mapper.UserDomainToDtoMapper;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUserByIdUseCase implements GetUserByIdUseCasePort {
  private final UserRepository userRepo;
  private final UserDomainToDtoMapper mapper;

  @Override
  public Result handle(Query q) {
    UUID userId = q.userID();
    User user =
        userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new Result(mapper.toDto(user));
  }
}
