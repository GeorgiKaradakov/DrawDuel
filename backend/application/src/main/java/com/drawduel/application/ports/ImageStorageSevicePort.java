package com.drawduel.application.ports;

import java.util.UUID;

public interface ImageStorageSevicePort {

  String uploadProfileImage(byte[] imageBytes, UUID userId);

  void deleteProfileImage(String imageUrl);
}
