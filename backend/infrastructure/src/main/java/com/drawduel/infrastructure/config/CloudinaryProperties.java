package com.drawduel.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cloudinary")
public class CloudinaryProperties {
  private String cloudName;
  private String apiKey;
  private String apiSecret;
}
