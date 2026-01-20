package com.drawduel.application.usecases;

import com.drawduel.application.ports.DeleteProfileImageUseCasePort;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProfileImageUseCase implements DeleteProfileImageUseCasePort {
  private final ImageStorageSevicePort imageStorageService;
  private final UserRepository userRepository;
  private final String DEFAULT_PROFILE_IMAGE_URL;

  @Override
  public Result handle(Query query) {

    User user =
        userRepository
            .findById(query.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (user.getProfileImageUrl() != null
        && user.getProfileImageUrl().equals(DEFAULT_PROFILE_IMAGE_URL)) {
      throw new IllegalStateException("Cannot delete default profile image!");
    }

    try {
      imageStorageService.deleteProfileImage(user.getProfileImageUrl());
    } catch (Exception e) {
      throw new IllegalStateException("Failed to delete profile image from storage", e);
    }

    user.setProfileImageUrl(DEFAULT_PROFILE_IMAGE_URL);
    userRepository.save(user);

    return new Result(user.getProfileImageUrl());
  }
}
