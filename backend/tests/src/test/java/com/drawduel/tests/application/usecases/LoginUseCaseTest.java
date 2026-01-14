package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  @BeforeEach
  void setup() {
    userRepo = mock(UserRepository.class);
    tokensService = mock(TokensService.class);
    hasher = mock(PasswordHasher.class);
    useCase = new LoginUseCase(userRepo, tokensService, hasher);
  }

  @Test
  void shouldLoginSuccessfullyWithEmail() {
    User user =
        new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hashed123", Instant.now(), null);
    when(userRepo.findByEmail("guts@drawduel.com")).thenReturn(Optional.of(user));
    when(hasher.matches("password123", "hashed123")).thenReturn(true);
    when(tokensService.generateTokens(user, "127.0.0.1", "Chrome", "Earth"))
        .thenReturn(new String[] {"access123", "refresh123"});

    var query =
        new LoginUseCasePort.Query(
            "guts@drawduel.com", "password123", "127.0.0.1", "Chrome", "Earth");

    var result = useCase.handle(query);

    assertThat(result.accessToken()).isEqualTo("access123");
    assertThat(result.refreshToken()).isEqualTo("refresh123");
    verify(userRepo).findByEmail("guts@drawduel.com");
    verify(tokensService).generateTokens(user, "127.0.0.1", "Chrome", "Earth");
  }

  @Test
  void shouldLoginSuccessfullyWithUsername() {
    User user =
        new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hash", Instant.now(), null);
    when(userRepo.findByEmail("guts")).thenReturn(Optional.empty());
    when(userRepo.findByUsername("guts")).thenReturn(Optional.of(user));
    when(hasher.matches("123", "hash")).thenReturn(true);
    when(tokensService.generateTokens(user, "127.0.0.1", "Chrome", "Earth"))
        .thenReturn(new String[] {"access", "refresh"});

    var query = new LoginUseCasePort.Query("guts", "123", "127.0.0.1", "Chrome", "Earth");

    var result = useCase.handle(query);

    assertThat(result.accessToken()).isEqualTo("access");
    assertThat(result.refreshToken()).isEqualTo("refresh");
    verify(userRepo).findByUsername("guts");
  }

  @Test
  void shouldThrowIfUserNotFound() {
    when(userRepo.findByEmail("unknown")).thenReturn(Optional.empty());
    when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

    var query = new LoginUseCasePort.Query("unknown", "pass", "127.0.0.1", "Chrome", "Earth");

    assertThatThrownBy(() -> useCase.handle(query))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid credentials");
  }

  @Test
  void shouldThrowIfPasswordDoesNotMatch() {
    User user =
        new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hash", Instant.now(), null);
    when(userRepo.findByEmail("guts@drawduel.com")).thenReturn(Optional.of(user));
    when(hasher.matches("wrong", "hash")).thenReturn(false);

    var query =
        new LoginUseCasePort.Query("guts@drawduel.com", "wrong", "127.0.0.1", "Chrome", "Earth");

    assertThatThrownBy(() -> useCase.handle(query))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid credentials");
  }
}
