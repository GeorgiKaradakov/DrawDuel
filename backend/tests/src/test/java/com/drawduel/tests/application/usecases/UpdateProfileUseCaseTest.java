package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.usecases.UpdateProfileUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateProfileUseCaseTest {

  private UserRepository userRepository;
  private UpdateProfileUseCase useCase;

  private UUID userId;
  private User user;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    useCase = new UpdateProfileUseCase(userRepository);

    userId = UUID.randomUUID();
    user = new User(userId, "oldUsername", "old@email.com", "hash", Instant.now(), null);
  }

  @Test
  void updatesProfileSuccessfully() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());

    useCase.handle(new UpdateProfileUseCasePort.Query(userId, "newUser", "new@email.com"));

    assertThat(user.getUsername()).isEqualTo("newUser");
    assertThat(user.getEmail()).isEqualTo("new@email.com");
    verify(userRepository).save(user);
  }

  @Test
  void throwsIfUsernameTaken() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername("taken"))
        .thenReturn(Optional.of(new User(UUID.randomUUID(), "user", "email", "hash", null, null)));

    assertThatThrownBy(
            () ->
                useCase.handle(
                    new UpdateProfileUseCasePort.Query(userId, "taken", "new@email.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Username already in use");
  }

  @Test
  void throwsIfEmailTaken() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
    when(userRepository.findByEmail("taken@email.com"))
        .thenReturn(Optional.of(new User(UUID.randomUUID(), "user", "email", "hash", null, null)));

    assertThatThrownBy(
            () ->
                useCase.handle(
                    new UpdateProfileUseCasePort.Query(userId, "newUser", "taken@email.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email already in use");
  }

  @Test
  void throwsIfUserNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> useCase.handle(new UpdateProfileUseCasePort.Query(userId, "x", "x@y.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User not found");
  }
}
