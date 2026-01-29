package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.mapper.UserDomainToDtoMapper;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginUseCaseTest {

  private UserRepository userRepo;
  private TokensService tokensService;
  private PasswordHasher hasher;
  private LoginUseCasePort useCase;
  private UserDomainToDtoMapper mapper;

  @BeforeEach
  void setup() {
    userRepo = mock(UserRepository.class);
    tokensService = mock(TokensService.class);
    hasher = mock(PasswordHasher.class);
    useCase = new LoginUseCase(userRepo, tokensService, hasher);
    mapper = mock(UserDomainToDtoMapper.class);
  }

  @Test
  void shouldLoginSuccessfullyWithEmail() {
    User user =
        new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hash", Instant.now(), null);
    UserDto userDto = mapper.toUserDto(user);

    when(userRepo.findByEmail("guts@drawduel.com")).thenReturn(Optional.of(user));
    when(hasher.matches("password", "hash")).thenReturn(true);

    when(tokensService.generateTokens(
            eq(user), eq("127.0.0.1"), eq("Chrome"), eq("Earth"), eq("Linux")))
        .thenReturn(new String[] {"access", "refresh"});

    var query =
        new LoginUseCasePort.Query(
            "guts@drawduel.com", "password", "127.0.0.1", "Chrome", "Earth", "Linux");

    var result = useCase.handle(query);

    assertThat(result.accessToken()).isEqualTo("access");
  }
}
