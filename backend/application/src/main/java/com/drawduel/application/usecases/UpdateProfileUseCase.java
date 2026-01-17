package com.drawduel.application.usecases;

import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;

public class UpdateProfileUseCase implements UpdateProfileUseCasePort {

  private final UserRepository userRepository;
  private final ImageStorageSevicePort imageStorageService;

  public UpdateProfileUseCase(
      UserRepository userRepository, ImageStorageSevicePort imageStorageService) {
    this.userRepository = userRepository;
    this.imageStorageService = imageStorageService;
  }

  @Override
  public Result handle(Query query) {
    User user =
        userRepository
            .findById(query.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (query.username() != null && !query.username().equals(user.getUsername())) {
      userRepository
          .findByUsername(query.username())
          .ifPresent(
              existing -> {
                if (!existing.getId().equals(user.getId())) {
                  throw new IllegalArgumentException("Username already in use");
                }
              });

      user.setUsername(query.username());
    }

    if (query.email() != null && !query.email().equals(user.getEmail())) {
      userRepository
          .findByEmail(query.email())
          .ifPresent(
              existing -> {
                if (!existing.getId().equals(user.getId())) {
                  throw new IllegalArgumentException("Email already in use");
                }
              });

      user.setEmail(query.email());
    }

    if (query.profileImage() != null) {
      String imageUrl = imageStorageService.uploadProfileImage(query.profileImage(), user.getId());
      user.setProfileImageUrl(imageUrl);
    }

    userRepository.save(user);

    return new Result(user.getUsername(), user.getEmail(), user.getProfileImageUrl());
  }
}
