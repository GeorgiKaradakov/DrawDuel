package com.drawduel.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.infrastructure.persistence.services.CloudinaryStorageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageStorageConfig {
  @Bean
  public Cloudinary cloudinary(@Value("${CLOUDINARY_URL}") String cloudinaryUrlEnv) {
    return new Cloudinary(cloudinaryUrlEnv);
  }

  @Bean
  public ImageStorageSevicePort imageStorageService(Cloudinary cloudinary) {
    return new CloudinaryStorageServiceImpl(cloudinary);
  }
}
