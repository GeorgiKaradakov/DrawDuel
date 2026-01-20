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
              "folder", "profile-images",
              "public_id", "user_" + userId,
              "overwrite", true,
              "resource_type", "image",
              "transformation", "c_fill,g_face,w_256,h_256");

      Map<?, ?> result = cloudinary.uploader().upload(imageBytes, options);

      Object secureUrl = result.get("secure_url");
      if (secureUrl == null) {
        throw new IllegalStateException("Cloudinary upload succeeded but secure_url was missing");
      }

      return secureUrl.toString();

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to upload profile image", e);
    }
  }

  @Override
  public void deleteProfileImage(String imageUrl) {
    String publicId = extractPublicId(imageUrl);

    if (publicId == null) {
      return;
    }

    try {
      cloudinary.uploader().destroy(publicId, Map.of("invalidate", true));
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to delete profile image", e);
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

  public String extractPublicId(String imageUrl) {
    if (imageUrl == null || imageUrl.isBlank()) {
      return null;
    }

    String cleanUrl = imageUrl.split("\\?")[0];
    String afterUpload = cleanUrl.substring(cleanUrl.indexOf("/upload/") + 8);

    afterUpload = afterUpload.replaceFirst("^v\\d+/", "");

    return afterUpload.replaceFirst("\\.[^.]+$", "");
  }
}
