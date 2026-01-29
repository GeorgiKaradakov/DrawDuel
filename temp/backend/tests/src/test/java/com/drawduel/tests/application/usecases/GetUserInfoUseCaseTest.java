package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.mapper.UserDomainToDtoMapper;
import com.drawduel.application.ports.GetUserInfoUseCasePort;
import com.drawduel.application.usecases.GetUserInfoUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetUserInfoUseCaseTest {

  private UserRepository userRepository;
  private UserDomainToDtoMapper mapper;
  private GetUserInfoUseCasePort useCase;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    mapper = mock(UserDomainToDtoMapper.class);
    useCase = new GetUserInfoUseCase(userRepository, mapper);
  }

  @Test
  void shouldReturnUserDto() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "u", "e", "h", null, null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(mapper.toUserDto(user)).thenReturn(mock(com.drawduel.application.dtos.UserDto.class));

    var result = useCase.handle(new GetUserInfoUseCasePort.Query(userId));

    assertThat(result.response()).isNotNull();
  }
}
