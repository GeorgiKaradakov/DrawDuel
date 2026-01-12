package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.usecases.DeleteAccountUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteAccountUseCaseTest {

  private UserRepository userRepository;
  private DeleteAccountUseCase useCase;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    useCase = new DeleteAccountUseCase(userRepository);
  }

  @Test
  void deletesUserSuccessfully() {
    UUID userId = UUID.randomUUID();

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(new User(UUID.randomUUID(), "user", "email", "hash", null)));

    useCase.handle(new DeleteAccountUseCasePort.Query(userId));

    verify(userRepository).deleteById(userId);
  }

  @Test
  void throwsIfUserNotFound() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.handle(new DeleteAccountUseCasePort.Query(userId)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User not found");
  }
}
