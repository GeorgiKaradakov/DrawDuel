package com.drawduel.application.usecases;

import com.drawduel.application.mapper.UserDomainToDtoMapper;
import com.drawduel.application.ports.GetUserInfoUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUserInfoUseCase implements GetUserInfoUseCasePort {
  public final UserRepository userRepository;
  public final UserDomainToDtoMapper userMapper;

  @Override
  public Result handle(Query query) {
    User user =
        userRepository
            .findById(query.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new Result(userMapper.toUserDto(user));
  }
}
