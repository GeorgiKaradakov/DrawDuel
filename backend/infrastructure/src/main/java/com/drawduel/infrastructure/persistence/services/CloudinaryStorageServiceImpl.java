package com.drawduel.infrastructure.persistence.services;

import com.cloudinary.Cloudinary;
import com.drawduel.application.ports.ImageStorageSevicePort;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloudinaryStorageServiceImpl implements ImageStorageSevicePort {

  private static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

  private final Cloudinary cloudinary;

  @Override
  public String uploadProfileImage(byte[] imageBytes, UUID userId) {
    validate(imageBytes, userId);

    try {
      Map<String, Object> options =
          Map.of(
              "folder",
              "profile-images",
              "public_id",
              "user_" + userId.toString(),
              "overwrite",
              true,
              "resource_type",
              "image",
              "transformation",
              Map.of("width", 256, "height", 256, "crop", "fill", "gravity", "face"));

      Map<?, ?> result = cloudinary.uploader().upload(imageBytes, options);

      Object secureUrl = result.get("secure_url");
      if (secureUrl == null) {
        throw new IllegalStateException("Cloudinary did not return secure_url");
      }

      return secureUrl.toString();

    } catch (Exception e) {
      throw new IllegalStateException("Failed to upload profile image", e);
    }
  }

  private void validate(byte[] imageBytes, UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User id must be provided");
    }

    if (imageBytes == null || imageBytes.length == 0) {
      throw new IllegalArgumentException("Image must not be empty");
    }

    if (imageBytes.length > MAX_IMAGE_SIZE_BYTES) {
      throw new IllegalArgumentException("Image exceeds maximum size of 2MB");
    }
  }
}
