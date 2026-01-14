package com.drawduel.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.infrastructure.persistence.services.CoudinaryStorageServiceImpl;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageStorageConfig {
  private final CloudinaryProperties cloudinaryProperties;

  @Autowired
  public ImageStorageConfig(CloudinaryProperties cloudinaryProperties) {
    this.cloudinaryProperties = cloudinaryProperties;
  }

  @Bean
  public Cloudinary cloudinary() {
    Map<?, ?> config =
        Map.of(
            "cloud_name", cloudinaryProperties.getCloudName(),
            "api_key", cloudinaryProperties.getApiKey(),
            "api_secret", cloudinaryProperties.getApiSecret());

    return new Cloudinary(config);
  }

  @Bean
  public ImageStorageSevicePort imageStorageService(Cloudinary cloudinary) {
    return new CoudinaryStorageServiceImpl(cloudinary);
  }
}
