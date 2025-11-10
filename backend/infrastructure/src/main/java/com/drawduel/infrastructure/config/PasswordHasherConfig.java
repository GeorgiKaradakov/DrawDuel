package com.drawduel.infrastructure.config;

import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.infrastructure.persistence.security.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordHasherConfig {
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public PasswordHasher passwordHasher(BCryptPasswordEncoder encoder) {
    return new BCryptPasswordHasher(encoder);
  }
}
