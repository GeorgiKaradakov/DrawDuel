package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.ChangePasswordUseCasePort;
import com.drawduel.application.usecases.ChangePasswordUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangePasswordUseCaseTest {

  private UserRepository userRepository;
  private PasswordHasher passwordHasher;
  private ChangePasswordUseCasePort useCase;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    passwordHasher = mock(PasswordHasher.class);
    useCase = new ChangePasswordUseCase(userRepository, passwordHasher);
  }

  @Test
  void shouldChangePasswordSuccessfully() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "user", "mail@test.com", "oldHash", Instant.now(), null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordHasher.matches("oldPass", "oldHash")).thenReturn(true);
    when(passwordHasher.hash("newPass")).thenReturn("newHash");

    useCase.handle(new ChangePasswordUseCasePort.Query(userId, "oldPass", "newPass"));

    assertThat(user.getPassHash()).isEqualTo("newHash");
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrowIfCurrentPasswordIncorrect() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "user", "mail@test.com", "hash", Instant.now(), null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordHasher.matches("wrong", "hash")).thenReturn(false);

    assertThatThrownBy(
            () -> useCase.handle(new ChangePasswordUseCasePort.Query(userId, "wrong", "newPass")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Incorrect password");
  }

  @Test
  void shouldThrowIfUserNotFound() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.handle(new ChangePasswordUseCasePort.Query(userId, "x", "y")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User not found");
  }
}
