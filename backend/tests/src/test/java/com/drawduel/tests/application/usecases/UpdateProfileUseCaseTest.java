package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.ImageStorageSevicePort;
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
  private ImageStorageSevicePort imageStorage;
  private UpdateProfileUseCase useCase;

  private UUID userId;
  private User user;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    imageStorage = mock(ImageStorageSevicePort.class);

    useCase = new UpdateProfileUseCase(userRepository, imageStorage);

    userId = UUID.randomUUID();
    user = new User(userId, "oldUsername", "old@email.com", "hash", Instant.now(), null);
  }

  // ✅ 1. Update username & email without image
  @Test
  void updatesProfileSuccessfully_withoutImage() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());

    var result =
        useCase.handle(
            new UpdateProfileUseCasePort.Query(userId, "newUser", "new@email.com", null));

    assertThat(user.getUsername()).isEqualTo("newUser");
    assertThat(user.getEmail()).isEqualTo("new@email.com");
    assertThat(user.getProfileImageUrl()).isNull();

    verify(userRepository).save(user);
    verify(imageStorage, never()).uploadProfileImage(any(), any());
  }

  // ✅ 2. Update username, email & profile image
  @Test
  void updatesProfileSuccessfully_withImage() {
    byte[] imageBytes = "image-bytes".getBytes();
    String imageUrl = "https://cloudinary.com/user.png";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
    when(imageStorage.uploadProfileImage(imageBytes, userId)).thenReturn(imageUrl);

    var result =
        useCase.handle(
            new UpdateProfileUseCasePort.Query(userId, "newUser", "new@email.com", imageBytes));

    assertThat(user.getProfileImageUrl()).isEqualTo(imageUrl);
    assertThat(result.profileImageUrl()).isEqualTo(imageUrl);

    verify(imageStorage).uploadProfileImage(imageBytes, userId);
    verify(userRepository).save(user);
  }

  // ❌ 3. User not found
  @Test
  void throwsIfUserNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> useCase.handle(new UpdateProfileUseCasePort.Query(userId, "x", "x@y.com", null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User not found");

    verifyNoInteractions(imageStorage);
  }

  // ❌ 4. Username already taken
  @Test
  void throwsIfUsernameAlreadyExists() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername("taken"))
        .thenReturn(
            Optional.of(
                new User(UUID.randomUUID(), "taken", "t@mail.com", "hash", Instant.now(), null)));

    assertThatThrownBy(
            () ->
                useCase.handle(
                    new UpdateProfileUseCasePort.Query(userId, "taken", "new@email.com", null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Username already in use");

    verify(imageStorage, never()).uploadProfileImage(any(), any());
  }

  // ❌ 5. Email already taken
  @Test
  void throwsIfEmailAlreadyExists() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
    when(userRepository.findByEmail("taken@email.com"))
        .thenReturn(
            Optional.of(
                new User(
                    UUID.randomUUID(), "user", "taken@email.com", "hash", Instant.now(), null)));

    assertThatThrownBy(
            () ->
                useCase.handle(
                    new UpdateProfileUseCasePort.Query(userId, "newUser", "taken@email.com", null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email already in use");

    verify(imageStorage, never()).uploadProfileImage(any(), any());
  }
}
