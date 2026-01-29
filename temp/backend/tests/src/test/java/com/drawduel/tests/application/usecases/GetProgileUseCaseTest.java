package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.usecases.GetProfileUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetProfileUseCaseTest {

  private UserRepository userRepository;
  private GetProfileUseCasePort useCase;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    useCase = new GetProfileUseCase(userRepository);
  }

  @Test
  void shouldReturnUserProfile() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "john", "john@test.com", "hash", null, null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    var result = useCase.handle(new GetProfileUseCasePort.Query(userId));

    assertThat(result.profile().getUsername()).isEqualTo("john");
    assertThat(result.profile().getEmail()).isEqualTo("john@test.com");
  }

  @Test
  void shouldThrowIfUserNotFound() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.handle(new GetProfileUseCasePort.Query(userId)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User not found");
  }
}
