package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.drawduel.application.mapper.UserDomainToDtoMapper;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.application.usecases.GetUserByIdUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetUserByIdUseCaseTest {
  private UserRepository userRepo;
  private UserDomainToDtoMapper mapper;
  private GetUserByIdUseCasePort useCase;

  @BeforeEach
  void setup() {
    userRepo = mock(UserRepository.class);
    mapper = mock(UserDomainToDtoMapper.class);
    useCase = new GetUserByIdUseCase(userRepo, mapper);
  }

  @Test
  void shouldReturnUserDtoWhenUserExists() {
    UUID id = UUID.randomUUID();
    User user = new User(id, "guts", "guts@drawduel.com", "hash", Instant.now());
    when(userRepo.findById(id)).thenReturn(Optional.of(user));
    when(mapper.toUserDto(user)).thenReturn(new com.drawduel.application.dtos.UserDto());

    var result = useCase.handle(new GetUserByIdUseCase.Query(id));

    assertThat(result.response()).isNotNull();
    verify(userRepo).findById(id);
  }
}
