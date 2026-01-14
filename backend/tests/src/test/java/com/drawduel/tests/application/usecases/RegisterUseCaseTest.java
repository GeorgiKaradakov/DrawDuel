package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterUseCaseTest {

  private UserRepository userRepo;
  private PasswordHasher hasher;
  private TokensService tokensService;
  private RegisterUseCasePort useCase;
  private ImageStorageSevicePort imageStorage;

  @BeforeEach
  void setup() {
    userRepo = mock(UserRepository.class);
    hasher = mock(PasswordHasher.class);
    tokensService = mock(TokensService.class);
    imageStorage = mock(ImageStorageSevicePort.class);
    useCase = new RegisterUseCase(userRepo, hasher, tokensService, imageStorage);
  }

  @Test
  void shouldRegisterUserSuccessfully() {
    RegisterRequestDto req =
        new RegisterRequestDto(
            "guts", "guts@drawduel.com", "123", "123", null, "127.0.0.1", "Chrome", "Earth");

    when(userRepo.findByEmail("guts@drawduel.com")).thenReturn(Optional.empty());
    when(userRepo.findByUsername("guts")).thenReturn(Optional.empty());
    when(hasher.hash("123")).thenReturn("hashed123");
    when(tokensService.generateTokens(any(User.class), eq("127.0.0.1"), eq("Chrome"), eq("Earth")))
        .thenReturn(new String[] {"accessToken", "refreshToken"});

    var result = useCase.handle(new RegisterUseCasePort.Query(req));

    assertThat(result.response().getAccessToken()).isEqualTo("accessToken");
    assertThat(result.response().getRefreshToken()).isEqualTo("refreshToken");
    verify(userRepo).save(any(User.class));
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {
    when(userRepo.findByEmail("taken@mail.com")).thenReturn(Optional.of(mock(User.class)));

    RegisterRequestDto req = new RegisterRequestDto();
    req.setEmail("taken@mail.com");
    req.setUsername("newUser");
    req.setPass("123");
    req.setRepeatPass("123");

    assertThatThrownBy(() -> useCase.handle(new RegisterUseCasePort.Query(req)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email already in use");
  }

  @Test
  void shouldThrowWhenUsernameAlreadyExists() {
    when(userRepo.findByEmail("free@mail.com")).thenReturn(Optional.empty());
    when(userRepo.findByUsername("taken")).thenReturn(Optional.of(mock(User.class)));

    RegisterRequestDto req = new RegisterRequestDto();
    req.setEmail("free@mail.com");
    req.setUsername("taken");
    req.setPass("123");
    req.setRepeatPass("123");

    assertThatThrownBy(() -> useCase.handle(new RegisterUseCasePort.Query(req)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Username already in use");
  }

  @Test
  void shouldThrowWhenPasswordsDoNotMatch() {
    RegisterRequestDto req = new RegisterRequestDto();
    req.setEmail("new@mail.com");
    req.setUsername("newUser");
    req.setPass("123");
    req.setRepeatPass("456");

    assertThatThrownBy(() -> useCase.handle(new RegisterUseCasePort.Query(req)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Passwords do not match");
  }
}
